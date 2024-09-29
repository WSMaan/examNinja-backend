package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.exception.ResourceNotFoundException;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.model.TestTable;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

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
    void getQuestionByTestId_ValidTestId_ReturnsQuestions() {
        // Given
        Long testId = 1L;
        int page = 0;
        int size = 1;
        TestTable testTable = new TestTable(); // Mock Test object
        Question question = new Question(); // Mock Question object
        Pageable pageable = PageRequest.of(page, size);
        Page<Question> questionPage = new PageImpl<>(Collections.singletonList(question), pageable, 1);

        // When
        when(testRepository.findById(testId)).thenReturn(Optional.of(testTable));
        when(questionRepository.findByTestTable(testTable, pageable)).thenReturn(questionPage);

        // Then
        Page<Question> result = questionService.getQuestionByTestId(testId, page, size);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(testRepository, times(1)).findById(testId);
        verify(questionRepository, times(1)).findByTestTable(testTable, pageable);
    }

    @Test
    void getQuestionByTestId_InvalidTestId_ThrowsResourceNotFoundException() {
        // Given
        Long testId = 1L;
        int page = 0;
        int size = 1;

        // When
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () -> questionService.getQuestionByTestId(testId, page, size));
        verify(testRepository, times(1)).findById(testId);
        verifyNoInteractions(questionRepository);
    }
}