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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testGetQuestionByTestId_ShouldReturnQuestions_WhenQuestionsExist() {
        // Arrange
        Long testId = 1L;
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);
        List<Question> questions = Arrays.asList(new Question());
        Page<Question> pagedQuestions = new PageImpl<>(questions, pageable, questions.size());

        // Mocking the repository call
        when(questionRepository.findByTestId(testId, pageable)).thenReturn(pagedQuestions);

        // Act
        Page<Question> result = questionService.getQuestionByTestId(testId, page, size);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.getContent().size());
        verify(questionRepository, times(1)).findByTestId(testId, pageable);  // Verify method call
    }

    @Test
    void testGetQuestionByTestId_ShouldThrowResourceNotFoundException_WhenNoQuestionsOnFirstPage() {
        // Arrange
        Long testId = 1L;
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        // Mocking an empty page result
        when(questionRepository.findByTestId(testId, pageable)).thenReturn(Page.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> questionService.getQuestionByTestId(testId, page, size)
        );

        assertEquals("No questions found for test with Test Id: " + testId, thrown.getMessage());
        verify(questionRepository, times(1)).findByTestId(testId, pageable);  // Verify method call
    }


    @Test
    void testGetQuestionByTestId_ShouldThrowPageOutOfBoundsException_WhenPageIsOutOfBounds() {
        // Arrange
        Long testId = 1L;
        int page = 1; // Requesting a page that exceeds the available pages
        int size = 1;

        // Create a Pageable for the page that will be requested
        Pageable pageable = PageRequest.of(page, size);

        // Create a single question to be returned on page 0
        List<Question> questionList = Collections.singletonList(new Question()); // One question
        Page<Question> pagedQuestions = new PageImpl<>(questionList, PageRequest.of(0, size), 1); // Total elements = 1

        // Mocking the repository call to return the page with questions
        when(questionRepository.findByTestId(testId, pageable)).thenReturn(pagedQuestions);

        // Act & Assert
        // Expecting an exception when trying to access an out-of-bounds page
        PageOutOfBoundsException thrown = assertThrows(PageOutOfBoundsException.class,
                () -> questionService.getQuestionByTestId(testId, page, size) // Call with page = 1
        );

        // Assert that the exception message contains the expected information
        assertTrue(thrown.getMessage().contains("Requested page is out of bounds"));

        // Verify that the repository method was called with the correct parameters
        verify(questionRepository, times(1)).findByTestId(testId, pageable);
    }

}
