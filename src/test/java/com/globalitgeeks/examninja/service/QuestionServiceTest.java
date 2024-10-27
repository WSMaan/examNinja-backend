package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.exception.PageOutOfBoundsException;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private AnswerService answerService;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestionByTestId_Success() {
        // Mock data
        Long testId = 1L;
        int page = 0;
        int size = 1;
        Long userId = 1L;

        TestTable test = new TestTable();
        test.setTestId(testId);
        test.setTestName("Sample Test");

        Question question = new Question();
        question.setQuestionId(100L);
        question.setQuestion("What is the capital of France?");
        question.setOption1("Berlin");
        question.setOption2("Madrid");
        question.setOption3("Paris");
        question.setOption4("Lisbon");
        question.setCorrectAnswer("c) Paris");
        question.setAnswerDescription("Paris is the capital of France.");
        question.setCategory("Geography");
        question.setLevel("Easy");
        question.setTestId(testId);

        // Mocking behavior
        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(questionRepository.findByTestId(testId, PageRequest.of(page, size))).thenReturn(new PageImpl<>(Arrays.asList(question)));

        // Calling the service method
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, size, userId);

        // Assertions
        assertEquals("Sample Test", response.get("testName"));
        assertEquals("1 of 1", response.get("questionNumber"));

        List<Map<String, Object>> questions = (List<Map<String, Object>>) response.get("questions");
        assertEquals(1, questions.size());
        assertEquals("What is the capital of France?", questions.get(0).get("question"));
        assertEquals("c) Paris", questions.get(0).get("correctAnswer"));
    }

    @Test
    void testGetQuestionByTestId_NoQuestionsFound() {
        Long testId = 1L;
        int page = 0;
        int size = 1;
        Long userId = 1L;

        TestTable test = new TestTable();
        test.setTestId(testId);
        test.setTestName("Sample Test");

        // Mocking behavior
        when(testRepository.findById(testId)).thenReturn(Optional.of(test));
        when(questionRepository.findByTestId(testId, PageRequest.of(page, size))).thenReturn(new PageImpl<>(List.of()));

        // Assertions
        assertThrows(ResourceNotFoundException.class, () -> {
            questionService.getQuestionByTestId(testId, page, size, userId);
        });
    }

    @Test
    void testGetQuestionByTestId_PageOutOfBounds() {
        Long testId = 1L;
        int page = 1; // Trying to access a non-existent page
        int size = 1;
        Long userId = 1L;

        // Mocking the TestTable
        TestTable test = new TestTable();
        test.setTestId(testId);
        test.setTestName("Sample Test");

        // Mocking behavior for testRepository
        when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        // Mocking behavior for questionRepository
        // Return an empty page for page 1 (out of bounds)
        when(questionRepository.findByTestId(testId, PageRequest.of(1, size)))
                .thenReturn(Page.empty());

        // Mocking behavior for page 0 to return a question
        Question question = new Question();
        question.setQuestionId(100L);
        question.setQuestion("What is the capital of France?");
        question.setOption1("Berlin");
        question.setOption2("Madrid");
        question.setOption3("Paris");
        question.setOption4("Lisbon");
        question.setCorrectAnswer("c) Paris");
        question.setAnswerDescription("Paris is the capital of France.");
        question.setCategory("Geography");
        question.setLevel("Easy");
        question.setTestId(testId);

        // This should return a page with the question on page 0
        when(questionRepository.findByTestId(testId, PageRequest.of(0, size)))
                .thenReturn(new PageImpl<>(Collections.singletonList(question), PageRequest.of(0, size), 1));

        // Assertions
        assertThrows(PageOutOfBoundsException.class, () -> {
            questionService.getQuestionByTestId(testId, page, size, userId);
        });
    }



    @Test
    void testGetQuestionByTestId_ResourceNotFound() {
        Long testId = 1L;
        int page = 0;
        int size = 1;
        Long userId = 1L;

        // Mocking behavior for Test not found
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        // Mocking behavior for questions found for the test ID
        when(questionRepository.findByTestId(testId, PageRequest.of(page, size)))
                .thenReturn(Page.empty()); // Return an empty Page here

        // Assertions
        assertThrows(ResourceNotFoundException.class, () -> {
            questionService.getQuestionByTestId(testId, page, size, userId);
        });
    }
}
