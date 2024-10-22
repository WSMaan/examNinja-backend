package com.globalitgeeks.examninja.service;


import com.globalitgeeks.examninja.ExamNinjaApplication;
import com.globalitgeeks.examninja.dto.AnswerDTO;
import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.ExamDataBaseOperationException;
import com.globalitgeeks.examninja.exception.InvalidExamDataException;
import com.globalitgeeks.examninja.exception.QuestionNotFoundException;
import com.globalitgeeks.examninja.model.ExamResult;
import com.globalitgeeks.examninja.model.ExamResultDetail;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.repository.ExamResultDetailRepository;
import com.globalitgeeks.examninja.repository.ExamResultRepository;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = ExamNinjaApplication.class) // Load Spring context with your main configuration class
public class ExamResultServiceTest {

    @InjectMocks
    private ExamResultService examResultService;

    @Mock
    private AnswerStorageService answerService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExamResultDetailRepository examResultDetailRepository;

    @Mock
    private ExamResultRepository examResultRepository;

    private Map<String, String> answersMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        answersMap = new HashMap<>();
    }

    // Helper method to prepare questions with given correct answers
    private void mockQuestion(Long questionId, String correctAnswer) {
        Question question = new Question(questionId, "Sample question", "A", "B", "C", "D", correctAnswer, null, null, null, null, 1L);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    }

    // Helper method to verify the saving of result details and summary
    private void verifySavingResults(int detailCount) {
        verify(examResultDetailRepository, times(detailCount)).save(any(ExamResultDetail.class));
        verify(examResultRepository, times(1)).save(any(ExamResult.class));
    }

    @Test
    public void testProcessSubmittedTest_CorrectlyCalculatesPercentage() {
        answersMap.put("1-1-101", "A");
        answersMap.put("1-1-102", "B");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        mockQuestion(101L, "A");
        mockQuestion(102L, "B");

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        assertEquals(100, response.getScore());
        assertEquals("PASS", response.getStatus());

        verifySavingResults(2);
    }

    @Test
    public void testProcessSubmittedTest_HalfCorrectAnswers() {
        answersMap.put("1-1-101", "A");
        answersMap.put("1-1-102", "C");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        mockQuestion(101L, "A");
        mockQuestion(102L, "B");

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        assertEquals(50, response.getScore());
        assertEquals("FAIL", response.getStatus());

        verifySavingResults(2);
    }

    @Test
    public void testProcessSubmittedTest_NoCorrectAnswers() {
        answersMap.put("1-1-101", "C");
        answersMap.put("1-1-102", "D");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        mockQuestion(101L, "A");
        mockQuestion(102L, "B");

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        assertEquals(0, response.getScore());
        assertEquals("FAIL", response.getStatus());

        verifySavingResults(2);
    }

    @Test
    public void testProcessSubmittedTest_NoAnswers() {
        when(answerService.getAllAnswers()).thenReturn(new HashMap<>());

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);

        Exception exception = assertThrows(InvalidExamDataException.class, () -> {
            examResultService.processSubmittedTest(request);
        });

        assertEquals("No valid answers found for the specified test.", exception.getMessage());

        verify(examResultRepository, never()).save(any(ExamResult.class));
    }

    @Test
    public void testProcessSubmittedTest_InvalidExamDataException() {
        when(answerService.getAllAnswers()).thenReturn(null);

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);
        Exception exception = assertThrows(InvalidExamDataException.class, () -> {
            examResultService.processSubmittedTest(request);
        });
        assertEquals("No answers found for the user.", exception.getMessage());
    }

    @Test
    public void testProcessSubmittedTest_QuestionNotFoundException() {
        answersMap.put("1-1-999", "A"); // Invalid question ID
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        when(questionRepository.findById(999L)).thenReturn(Optional.empty());

        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);
        Exception exception = assertThrows(QuestionNotFoundException.class, () -> {
            examResultService.processSubmittedTest(request);
        });
        assertEquals("Question not found: 999", exception.getMessage());
    }

    @Test
    public void testProcessSubmittedTest_ExamResultDetailDatabaseOperationException() {
        // Prepare the test data
        answersMap.put("1-1-101", "A"); // Simulating userId 1, testId 1, questionId 101 with answer A
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        // Mocking a Question entity for questionId 101
        Question question = new Question();
        question.setQuestionId(101L);
        question.setCorrectAnswer("A"); // The correct answer is A
        when(questionRepository.findById(101L)).thenReturn(Optional.of(question));

        // Simulating a database error when saving ExamResultDetail
        doThrow(new RuntimeException("Database error")).when(examResultDetailRepository).save(any(ExamResultDetail.class));

        // Create the request
        ExamSubmissionRequest request = new ExamSubmissionRequest();
        request.setTestId(1L);
        request.setId(1L);
        request.setAnswers(Arrays.asList(new AnswerDTO(101L, "A"))); // AnswerDTO should match your implementation

        // Assert that the ExamDataBaseOperationException is thrown
        ExamDataBaseOperationException exception = assertThrows(ExamDataBaseOperationException.class, () -> {
            examResultService.processSubmittedTest(request);
        });

        // Verify the error message
        assertEquals("Failed to save exam result detail: ", exception.getMessage());
    }


    @Test
    public void testProcessSubmittedTest_ExamResultDatabaseOperationException() {
        // Prepare the test data
        answersMap.put("1-1-101", "A");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        mockQuestion(101L, "A");

        // Simulate database failure when saving ExamResult
        doThrow(new RuntimeException("Database error")).when(examResultRepository).save(any(ExamResult.class));

        // Create the request
        ExamSubmissionRequest request = new ExamSubmissionRequest(1L, 1L, null);

        // Expect the custom message for ExamResult
        Exception exception = assertThrows(ExamDataBaseOperationException.class, () -> {
            examResultService.processSubmittedTest(request);
        });

        // Verify that the message matches
        assertEquals("Failed to save exam result: ", exception.getMessage());
    }


}











