package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.exception.PageOutOfBoundsException;
import com.globalitgeeks.examninja.exception.ResourceNotFoundException;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.model.TestTable;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.globalitgeeks.examninja.service.AnswerService;
import com.globalitgeeks.examninja.model.TestTable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private AnswerService answerService;
    private static final int FIRST_PAGE = 0;

    public Map<String, Object> getQuestionByTestId(Long testId, int page, int size, Long userId) {
        // Fetch one question per request based on the test id
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = questionRepository.findByTestId(testId, pageable);

        if (questionPage.isEmpty() && page == FIRST_PAGE) {
            throw new ResourceNotFoundException("No questions found for test with Test Id: " + testId);
        } else if (page > questionPage.getTotalPages() - 1) {
            throw new PageOutOfBoundsException("Requested page is out of bounds. Maximum page number: " + (questionPage.getTotalPages() - 1));
        }

        // Total number of questions
        long totalQuestions = questionPage.getTotalElements();

        // Calculate the current question number (since page is 0-based)
        int questionNumber = (page * questionPage.getSize()) + 1;

        // Check if this is the last page
        boolean isLastPage = questionPage.isLast();

        // Fetch the test name
        TestTable test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found with Test Id: " + testId));
        String testName = test.getTestName();

        // Prepare the response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("testName", testName);
        response.put("questionNumber", questionNumber + " of " + totalQuestions);

        // Add questions with selected option details
        List<Map<String, Object>> questionsWithSelectedOptions = questionPage.getContent().stream().map(question -> {
            Map<String, Object> questionDetails = new LinkedHashMap<>();
            questionDetails.put("questionId", question.getQuestionId());
            questionDetails.put("question", question.getQuestion());
            questionDetails.put("option1", question.getOption1());
            questionDetails.put("option2", question.getOption2());
            questionDetails.put("option3", question.getOption3());
            questionDetails.put("option4", question.getOption4());
            questionDetails.put("correctAnswer", question.getCorrectAnswer());
            questionDetails.put("answerDescription", question.getAnswerDescription());
            questionDetails.put("category", question.getCategory());
            questionDetails.put("level", question.getLevel());
            questionDetails.put("questionType", question.getQuestionType());
            questionDetails.put("testId", question.getTestId());

            // Get previously selected option if available
            Map<String, String> selectedOption = AnswerService.getAnswer(userId, testId, question.getQuestionId());
            response.put("selectedOption", selectedOption != null ? selectedOption : null);

            return questionDetails;
        }).toList();

        response.put("questions", questionsWithSelectedOptions);

        // Page details using LinkedHashMap to preserve order
        Map<String, Object> pageDetails = new LinkedHashMap<>();
        pageDetails.put("pageNumber", questionPage.getNumber());
        pageDetails.put("pageSize", questionPage.getSize());
        pageDetails.put("totalPages", questionPage.getTotalPages());
        pageDetails.put("totalElements", totalQuestions);
        pageDetails.put("lastPage", isLastPage);

        response.put("pageDetails", pageDetails);

        return response;
    }

}
