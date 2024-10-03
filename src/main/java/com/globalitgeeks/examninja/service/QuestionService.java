package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.exception.PageOutOfBoundsException;
import com.globalitgeeks.examninja.exception.ResourceNotFoundException;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    public Page<Question> getQuestionByTestId(Long testId, int page, int size) {

        // Fetch one question per request based on the test id
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questions = questionRepository.findByTestId(testId, pageable);
        if (questions.isEmpty() && page == 0) {
            // If there are no questions at all, and it's the first page
            throw new ResourceNotFoundException("No questions found for test with Test Id: " + testId);
        } else if (page > questions.getTotalPages() - 1) {
            // If the page number requested exceeds the available pages
            throw new PageOutOfBoundsException("Requested page is out of bounds. Maximum page number: " + (questions.getTotalPages() - 1));
        }
        return questions;
    }

}
