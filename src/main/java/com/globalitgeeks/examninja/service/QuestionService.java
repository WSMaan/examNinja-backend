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

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;
    private static final int FIRST_PAGE = 0;

    public Map<String, Object> getQuestionByTestId(Long testId, int page, int size) {
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

        // Prepare the response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("questionNumber", questionNumber + " of " + totalQuestions);
        response.put("questions", questionPage.getContent());

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
