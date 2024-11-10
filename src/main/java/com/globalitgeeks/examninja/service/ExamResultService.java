
package com.globalitgeeks.examninja.service;


import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.ExamDataBaseOperationException;
import com.globalitgeeks.examninja.exception.InvalidExamDataException;
import com.globalitgeeks.examninja.exception.QuestionNotFoundException;
import com.globalitgeeks.examninja.model.ExamResult;
import com.globalitgeeks.examninja.model.ExamResultDetail;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.ExamResultDetailRepository;
import com.globalitgeeks.examninja.repository.ExamResultRepository;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamResultService {
    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamResultDetailRepository examResultDetailRepository;

    @Autowired
    private ExamResultRepository examResultRepository;

    private static final int pass_Marks_Cut_Off = 65;

   public ExamResultResponse processSubmittedTest(ExamSubmissionRequest request,Long userId) {
       Long testId = request.getTestId();
       Long ExtractedUserId = userId;

       // Combine submissionDate and submissionTime from request
       LocalDateTime submissionDateTime = request.getSubmissionDateTime();

       // Fetch all answers from AnswerService
       Map<String, String> allAnswers = answerService.getAllAnswers();
       if (allAnswers.isEmpty()) {
           throw new InvalidExamDataException("No answers found for the user.");
       }

       // Filter answers for the specific student and test
       Map<Long, String> studentTestAnswers = filterStudentTestAnswers(allAnswers, userId, testId);

       // Process answers and calculate the result
       int totalQuestions = studentTestAnswers.size();
       int correctAnswers = 0;


       if (totalQuestions == 0) {
           // If there are no answers, return a score of 0 and fail status
           return new ExamResultResponse(testId, userId, 0, pass_Marks_Cut_Off, "FAIL");
       }

       // Reuse the logic to save results and get the count of correct answers
       correctAnswers = saveCompareAndStoreAnswer(studentTestAnswers, testId, userId, submissionDateTime);

       // Calculate percentage
       double percentage = ((double) correctAnswers / totalQuestions) * 100;
       String status = (percentage >= pass_Marks_Cut_Off) ? "PASS" : "FAIL";

       // Save summary result
       ExamResult summaryResult = new ExamResult();
       summaryResult.setTestId(testId);
       summaryResult.setId(userId);
       summaryResult.setScore(percentage);
       summaryResult.setStatus(status);
       summaryResult.setSubmissionDateTime(submissionDateTime);

       try {
           examResultRepository.save(summaryResult);
       } catch (Exception e) {
           throw new ExamDataBaseOperationException("Failed to save exam result: " + e.getMessage());
       }


       return new ExamResultResponse(testId, userId, (int) percentage, pass_Marks_Cut_Off, status);
   }

    private int saveCompareAndStoreAnswer(Map<Long, String> studentTestAnswers, Long testId, Long userId, LocalDateTime submissionDateTime) {
        int correctAnswers = 0;

        for (Map.Entry<Long, String> entry : studentTestAnswers.entrySet()) {
            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            // Fetch the correct answer from the database using QuestionRepository
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new QuestionNotFoundException("Question not found: " + questionId));

            String correctAnswer = question.getCorrectAnswer();
            if (submittedAnswer.equals(correctAnswer)) {
                correctAnswers++;
            }

            // Save detailed result for this question
            ExamResultDetail resultDetail = new ExamResultDetail();
            resultDetail.setTestId(testId);
            resultDetail.setId(userId);
            resultDetail.setQuestionId(questionId);
            resultDetail.setSubmittedAnswer(submittedAnswer);
            resultDetail.setCorrectAnswer(correctAnswer);
            resultDetail.setSubmissionDateTime(submissionDateTime);

            try {
                examResultDetailRepository.save(resultDetail);
            } catch (Exception e) {
                throw new ExamDataBaseOperationException("Failed to save exam result detail: " + e.getMessage());
            }
        }

        return correctAnswers;
    }

    private Map<Long, String> filterStudentTestAnswers(Map<String, String> allAnswers, Long userId, Long testId) {
        Map<Long, String> studentTestAnswers = new HashMap<>();
        allAnswers.forEach((key, value) -> {
            String[] keyParts = key.split("-");
            Long storedUserId = Long.parseLong(keyParts[0]);
            Long storedTestId = Long.parseLong(keyParts[1]);
            Long questionId = Long.parseLong(keyParts[2]);

            if (storedUserId.equals(userId) && storedTestId.equals(testId)) {
                studentTestAnswers.put(questionId, value);
            }
        });

        if (studentTestAnswers.isEmpty()) {
            throw new InvalidExamDataException("No valid answers found for the specified test.");
        }

        return studentTestAnswers;
    }
}

