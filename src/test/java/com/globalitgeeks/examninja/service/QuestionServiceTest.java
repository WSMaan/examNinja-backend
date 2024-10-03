package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.exception.PageOutOfBoundsException;
import com.globalitgeeks.examninja.exception.ResourceNotFoundException;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestionByTestId_Success() {
        Long testId = 1L;
        int page = 0;
        int size = 1;

        // Mocking a list of questions
        Question question = new Question(); // Initialize with required properties
        List<Question> questions = Collections.singletonList(question);

        // Mocking the behavior of the repository
        Page<Question> questionPage = new PageImpl<>(questions);
        when(questionRepository.findByTestId(testId, Pageable.ofSize(size))).thenReturn(questionPage);

        // Call the method to be tested
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, size);

        // Assertions
        assertNotNull(response);
        assertEquals("1 of 1", response.get("questionNumber"));
        assertEquals(1L, ((Map<?, ?>) response.get("pageDetails")).get("totalElements"));
    }

    @Test
    void testGetQuestionByTestId_NoQuestionsFound_FirstPage() {
        Long testId = 1L;
        int page = 0;
        int size = 1;

        // Mocking the behavior of the repository for an empty page
        Page<Question> emptyPage = new PageImpl<>(Collections.emptyList());
        when(questionRepository.findByTestId(testId, Pageable.ofSize(size))).thenReturn(emptyPage);

        // Call and assert exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            questionService.getQuestionByTestId(testId, page, size);
        });

        assertEquals("No questions found for test with Test Id: " + testId, exception.getMessage());
    }

    @Test
    void testGetQuestionByTestId_PageOutOfBounds() {
        Long testId = 1L;
        int page = 1; // This page is out of bounds
        int size = 1;

        // Mocking a page with only one question
        Question question = new Question(); // Initialize with required properties
        Page<Question> questionPage = new PageImpl<>(Collections.singletonList(question), PageRequest.of(0, size), 1); // Set up page with size and total elements

        // Mocking the repository to return the page with one question
        when(questionRepository.findByTestId(testId, PageRequest.of(page, size))).thenReturn(questionPage);

        // Call and assert exception
        Exception exception = assertThrows(PageOutOfBoundsException.class, () -> {
            questionService.getQuestionByTestId(testId, page, size);
        });

        assertEquals("Requested page is out of bounds. Maximum page number: 0", exception.getMessage());
    }

}
