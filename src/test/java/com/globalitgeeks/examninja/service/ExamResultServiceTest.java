
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
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


//@SpringBootTest(classes = ExamNinjaApplication.class)
@ExtendWith(MockitoExtension.class)
public class ExamResultServiceTest {

    @InjectMocks
    private ExamResultService examResultService;

    @Mock
    private AnswerService answerService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExamResultDetailRepository examResultDetailRepository;

    @Mock
    private ExamResultRepository examResultRepository;

    private Map<String, String> answersMap;

    @BeforeEach
    void setUp() {
        answersMap = new HashMap<>();
    }

    private void mockQuestion(Long questionId, String correctAnswer) {
        Question question = new Question(questionId, "Sample question", "A", "B", "C", "D", correctAnswer, null, null, null, null, 1L);
        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));
    }

    private void verifySavingResults(int detailCount) {
        verify(examResultDetailRepository, times(detailCount)).save(any(ExamResultDetail.class));
        verify(examResultRepository, times(1)).save(any(ExamResult.class));
    }

    @Test
    public void testProcessSubmittedTest_CorrectlyCalculatesPercentage() {
        answersMap.put("1-1-101", "A");
        answersMap.put("1-1-102", "B");
        // Mock the static method getAllAnswers using try-with-resources
        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(answersMap);

            // Mock Question count
            when(questionRepository.countByTestId(1L)).thenReturn(2L);

            // Mock questions with incorrect answers for the test case
            mockQuestion(101L, "A");
            mockQuestion(102L, "B");

            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;

            // Prepare the request and call the service method
            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);
            ExamResultResponse response = examResultService.processSubmittedTest(request, userId);

            // Assertions
            assertEquals(100.0, response.getScore(), 0.01);  // Use delta for floating point comparison
            assertEquals("PASS", response.getStatus());

            // Verify saving results
            verifySavingResults(2);
        }
    }

    @Test
    public void testProcessSubmittedTest_HalfCorrectAnswers() {
        answersMap.put("1-1-101", "A");
        answersMap.put("1-1-102", "C");
        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(answersMap);

            // Mock Question count
            when(questionRepository.countByTestId(1L)).thenReturn(2L);


            mockQuestion(101L, "A");
            mockQuestion(102L, "B");

            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;


            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);
            ExamResultResponse response = examResultService.processSubmittedTest(request, userId);

            assertEquals(50, response.getScore());
            assertEquals("FAIL", response.getStatus());

            verifySavingResults(2);
        }
    }


    @Test
    public void testProcessSubmittedTest_NoCorrectAnswers() {
        answersMap.put("1-1-101", "C");
        answersMap.put("1-1-102", "D");
        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(answersMap);

            // Mock Question count
            when(questionRepository.countByTestId(1L)).thenReturn(2L);


            mockQuestion(101L, "A");
            mockQuestion(102L, "B");


            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;

            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);
            ExamResultResponse response = examResultService.processSubmittedTest(request, userId);

            assertEquals(0, response.getScore());
            assertEquals("FAIL", response.getStatus());

            verifySavingResults(2);
        }
    }


    @Test
    public void testProcessSubmittedTest_NoAnswers() {

        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(new HashMap<>());

            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;

            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);

            Exception exception = assertThrows(InvalidExamDataException.class, () -> {
                examResultService.processSubmittedTest(request, userId);
            });

            assertEquals("No answers found for the user.", exception.getMessage());

            verify(examResultRepository, never()).save(any(ExamResult.class));
        }
    }


    @Test
    public void testProcessSubmittedTest_InvalidExamDataException() {
        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(new HashMap<>());


            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;

            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);
            Exception exception = assertThrows(InvalidExamDataException.class, () -> {
                examResultService.processSubmittedTest(request, userId);
            });
            assertEquals("No answers found for the user.", exception.getMessage());
        }
    }


    @Test
    public void testProcessSubmittedTest_QuestionNotFoundException() {
        answersMap.put("1-1-999", "A"); // Ensure correct IDs are used

        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(answersMap);

            // Simulate questionRepository returning empty for question ID 999
            when(questionRepository.findById(999L)).thenReturn(Optional.empty());

            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;

            // Request with matching testId
            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);

            // Check that QuestionNotFoundException is thrown
            Exception exception = assertThrows(QuestionNotFoundException.class, () -> {
                examResultService.processSubmittedTest(request, userId);
            });

            assertEquals("Question not found: 999", exception.getMessage());
        }
    }


    @Test
    public void testProcessSubmittedTest_ExamResultDetailDatabaseOperationException() {
        answersMap.put("1-1-101", "A");
        try (MockedStatic<AnswerService> mockedStatic = Mockito.mockStatic(AnswerService.class)) {
            mockedStatic.when(AnswerService::getAllAnswers).thenReturn(answersMap);

            // Mock question to return
            mockQuestion(101L, "A");

            // Simulate a database exception when saving ExamResultDetail
            doThrow(new RuntimeException("Database error")).when(examResultDetailRepository).save(any(ExamResultDetail.class));

            LocalDateTime currentDateTime = LocalDateTime.now();
            Long userId = 1L;
            ExamSubmissionRequest request = new ExamSubmissionRequest(1L, currentDateTime);

            // Expecting an exception to be thrown
            Exception exception = assertThrows(ExamDataBaseOperationException.class, () -> {
                examResultService.processSubmittedTest(request, userId);
            });

            assertEquals("Failed to save exam result detail: Database error", exception.getMessage());
        }
    }
}
