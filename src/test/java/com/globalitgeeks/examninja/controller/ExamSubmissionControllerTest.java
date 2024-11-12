
package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.InvalidExamRequestException;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.ExamResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ExamSubmissionControllerTest {

    @InjectMocks
    private TestController examSubmissionController;

    @Mock
    private ExamResultService examResultService;

    @Mock
    private JwtUtil jwtUtil;

    private static final String TOKEN = "Bearer mockToken";
    private static final Long MOCK_USER_ID = 1L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testSubmitTest_Success() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        ExamResultResponse expectedResponse = new ExamResultResponse(1L, MOCK_USER_ID, 100, 65, "PASS");

        when(jwtUtil.extractUserId(TOKEN)).thenReturn(MOCK_USER_ID);
        when(examResultService.processSubmittedTest(request, MOCK_USER_ID)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ExamResultResponse> response = examSubmissionController.submitTest(request, TOKEN);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(examResultService, times(1)).processSubmittedTest(request, MOCK_USER_ID);
    }


    @Test
    public void testSubmitTest_HandlesCustomException() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(MOCK_USER_ID);
        when(examResultService.processSubmittedTest(any(), anyLong()))
                .thenThrow(new InvalidExamRequestException("Invalid Test Id"));

        // Act & Assert
        InvalidExamRequestException exception = assertThrows(InvalidExamRequestException.class, () -> {
            examSubmissionController.submitTest(request, TOKEN);
        });
        assertEquals("Invalid Test Id", exception.getMessage());
    }

    @Test
    public void testSubmitTest_Failure() {
        // Arrange
        ExamSubmissionRequest request = createValidRequest();
        when(jwtUtil.extractUserId(TOKEN)).thenReturn(MOCK_USER_ID);
        when(examResultService.processSubmittedTest(any(), anyLong()))
                .thenThrow(new RuntimeException("Processing Error"));


        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            examSubmissionController.submitTest(request, TOKEN);
        });
        assertEquals("Processing Error", exception.getMessage());
        verify(examResultService, times(1)).processSubmittedTest(request, MOCK_USER_ID);
    }


    // Helper method to create a valid request
    private ExamSubmissionRequest createValidRequest() {
        ExamSubmissionRequest request = new ExamSubmissionRequest();
        request.setTestId(1L);
        return request;
    }
}

