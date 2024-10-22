
package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.AnswerDTO;
import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.InvalidExamRequestException;
import com.globalitgeeks.examninja.service.ExamResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class ExamSubmissionControllerTest {

    @InjectMocks
    private ExamSubmissionController examSubmissionController;

    @Mock
    private ExamResultService examResultService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSubmitTest_Success() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();

        ExamResultResponse expectedResponse = new ExamResultResponse(1L, 1L, 100, 65, "PASS");
        when(examResultService.processSubmittedTest(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ExamResultResponse> response = examSubmissionController.submitTest(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(examResultService, times(1)).processSubmittedTest(request);
    }

    @Test
    public void testSubmitTest_MissingFields() {
        // Arrange
        ExamSubmissionRequest request = new ExamSubmissionRequest(); // Empty request

        // Act & Assert
        InvalidExamRequestException exception = assertThrows(InvalidExamRequestException.class, () -> {
            examSubmissionController.submitTest(request);
        });

        // Assert the exception message for missing fields
        assertEquals("Request is missing required fields: testId, id, or answers.", exception.getMessage());
    }

    @Test
    public void testSubmitTest_InvalidInput() {
        // Arrange
        ExamSubmissionRequest request = new ExamSubmissionRequest();
        request.setTestId(null); // Invalid testId
        request.setId(1L);      // Valid id

        // Act & Assert
        InvalidExamRequestException exception = assertThrows(InvalidExamRequestException.class, () -> {
            examSubmissionController.submitTest(request);
        });

        assertEquals("Request is missing required fields: testId, id, or answers.", exception.getMessage());
    }

    @Test
    public void testSubmitTest_HandlesCustomException() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        when(examResultService.processSubmittedTest(any())).thenThrow(new InvalidExamRequestException("Invalid Test Id"));

        // Act & Assert
        InvalidExamRequestException exception = assertThrows(InvalidExamRequestException.class, () -> {
            examSubmissionController.submitTest(request);
        });
        assertEquals("Invalid Test Id", exception.getMessage());
    }

    @Test
    public void testSubmitTest_Failure() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        when(examResultService.processSubmittedTest(any())).thenThrow(new RuntimeException("Processing Error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            examSubmissionController.submitTest(request);
        });
        assertEquals("Processing Error", exception.getMessage());
        verify(examResultService, times(1)).processSubmittedTest(request);
    }

    @Test
    public void testSubmitTest_CorrectContentType() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        ExamResultResponse expectedResponse = new ExamResultResponse(1L, 1L, 100, 65, "PASS");
        when(examResultService.processSubmittedTest(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ExamResultResponse> response = examSubmissionController.submitTest(request);

        // Assert
        assertEquals("application/json", response.getHeaders().getContentType().toString());
    }

    // Helper method to create a valid request
    private ExamSubmissionRequest createValidRequest() {
        ExamSubmissionRequest request = new ExamSubmissionRequest();
        request.setTestId(1L);
        request.setId(1L);
        List<AnswerDTO> answers = new ArrayList<>();
        answers.add(new AnswerDTO(1L, "A"));
        answers.add(new AnswerDTO(2L, "B"));
        request.setAnswers(answers);
        return request;
    }
}
