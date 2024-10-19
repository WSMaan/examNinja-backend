
package com.globalitgeeks.examninja.service;


import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExamResultService {
    @Autowired
    private AnswerStorageService answerService;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamResultDetailRepository examResultDetailRepository;
    @Autowired
    private ExamResultRepository examResultRepository;
    /*public ExamResultResponse processSubmittedTest(ExamSubmissionRequest request) {
        Long testId = request.getTestId();
        Long userId = request.getId();

        // Fetch all answers from AnswerService
        Map<String, String> allAnswers = answerService.getAllAnswers();

        // Filter answers for the specific student and test
        Map<Long, String> studentTestAnswers = new HashMap<>();
        allAnswers.forEach((key, value) -> {
            String[] keyParts = key.split("-");
            Long storedStudentId = Long.parseLong(keyParts[0]);
            Long storedTestId = Long.parseLong(keyParts[1]);
            Long questionId = Long.parseLong(keyParts[2]);

            if (storedStudentId.equals(userId) && storedTestId.equals(testId)) {
                studentTestAnswers.put(questionId, value);  // Store questionId and answer
            }
        });

        // Process answers and calculate the result
        int totalQuestions = studentTestAnswers.size();
        int correctAnswers = 0;

        for (Map.Entry<Long, String> entry : studentTestAnswers.entrySet()) {
            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            // Fetch the correct answer from the database using QuestionRepository
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            String correctAnswer = question.getCorrectAnswer();

            // Compare the submitted answer with the correct answer
            if (submittedAnswer.equals(correctAnswer)) {
                correctAnswers++;
            }

            // Save detailed result for this question
            ExamResultDetail resultDetail = new ExamResultDetail(null,testId, userId, questionId, submittedAnswer, correctAnswer);
            examResultDetailRepository.save(resultDetail);
        }

        // Calculate percentage
        double percentage = ((double) correctAnswers / totalQuestions) * 100;
        String status = (percentage >= 65) ? "PASS" : "FAIL";

        // Save summary result
        ExamResult summaryResult = new ExamResult(null,testId, userId, percentage, status);
        examResultRepository.save(summaryResult);

        // Return response
        return new ExamResultResponse(testId, userId, (int) percentage, 65, status);
    }
}*/
    public ExamResultResponse processSubmittedTest(ExamSubmissionRequest request) {
        Long testId = request.getTestId();
        Long userId = request.getId();

        // Fetch all answers from AnswerService
        Map<String, String> allAnswers = answerService.getAllAnswers();

        // Log the fetched answers for debugging
        System.out.println("All Answers: " + allAnswers);

        // Filter answers for the specific student and test
        Map<Long, String> studentTestAnswers = new HashMap<>();
        allAnswers.forEach((key, value) -> {
            String[] keyParts = key.split("-");
            Long storedStudentId = Long.parseLong(keyParts[0]);
            Long storedTestId = Long.parseLong(keyParts[1]);
            Long questionId = Long.parseLong(keyParts[2]);

            // Check for matching student ID and test ID
            if (storedStudentId.equals(userId) && storedTestId.equals(testId)) {
                studentTestAnswers.put(questionId, value);  // Store questionId and answer
            }
        });

        // Log the filtered student test answers for debugging
        System.out.println("Student Test Answers: " + studentTestAnswers);

        // Process answers and calculate the result
        int totalQuestions = studentTestAnswers.size();
        int correctAnswers = 0;

        if (totalQuestions == 0) {
            // If there are no answers, return a score of 0 and fail status
            return new ExamResultResponse(testId, userId, 0, 65, "FAIL");
        }

        for (Map.Entry<Long, String> entry : studentTestAnswers.entrySet()) {
            Long questionId = entry.getKey();
            String submittedAnswer = entry.getValue();

            // Fetch the correct answer from the database using QuestionRepository
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            String correctAnswer = question.getCorrectAnswer(); // Should fetch the correct answer now
            System.out.println("Submitted Answer: " + submittedAnswer + ", Correct Answer: " + correctAnswer);

            // Check if the correct answer is not null
            if (correctAnswer == null) {
                System.out.println("Warning: Correct answer for question ID " + questionId + " is null!");
            }

            // Compare the submitted answer with the correct answer
            if (submittedAnswer.equals(correctAnswer)) {
                correctAnswers++;
            }

            // Save detailed result for this question
            ExamResultDetail resultDetail = new ExamResultDetail(null, testId, userId, questionId, submittedAnswer, correctAnswer);
            examResultDetailRepository.save(resultDetail);
        }

        // Calculate percentage
        double percentage = ((double) correctAnswers / totalQuestions) * 100;
        String status = (percentage >= 65) ? "PASS" : "FAIL";

        // Save summary result
        ExamResult summaryResult = new ExamResult(null, testId, userId, percentage, status);
        examResultRepository.save(summaryResult);

        // Return response
        return new ExamResultResponse(testId, userId, (int) percentage, 65, status);
    }
    }

