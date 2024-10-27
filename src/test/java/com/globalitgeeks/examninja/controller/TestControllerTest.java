package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.ApiResponse;
import com.globalitgeeks.examninja.dto.StoreAnswer;
import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.AnswerService;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

class TestControllerTest {

    @InjectMocks
    private TestController testController;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private QuestionService questionService;
    @Mock
    private TestService testService;
    @Mock
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestionByTestId() {
        Long testId = 1L;
        int page = 0;
        String token = "Bearer mock-jwt-token";  // Mock token for testing

        // Mock user ID extracted from the token
        Long mockUserId = 1L; // Sample user ID

        // Mocking the response from the QuestionService
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("questionNumber", "1 of 60");
        mockResponse.put("questions", new Object[] {}); // Mock question objects as needed
        mockResponse.put("selectedOption", null); // Assuming no selection initially

        // Mocking the service method
        when(jwtUtil.extractUserId("mock-jwt-token")).thenReturn(mockUserId);  // Mock user ID extraction
        when(questionService.getQuestionByTestId(testId, page, 1, mockUserId)).thenReturn(mockResponse);

        // Call the method with the token in the request header
        ResponseEntity<Map<String, Object>> responseEntity = testController.getQuestionByTestId(testId, page, token);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    public void testGetTestsForUser() {
        // Arrange: Create mock data for the TestDto list
        List<TestDto> mockTestList = Arrays.asList(
                new TestDto(1L, "Foundation Test", 50L),
                new TestDto(2L, "Advanced Test", 30L)
        );
        String token = "Bearer mock-jwt-token";  // Mock token for testing


        // Mock the behavior of testService
        when(testService.getTestsForUser(1L)).thenReturn(mockTestList);
        when(jwtUtil.extractUserId("mock-jwt-token")).thenReturn(1L);  // Mock user ID extraction


        // Act: Call the controller method
        ResponseEntity<List<TestDto>> response = testController.getTestsForUser(token);

        // Assert: Verify the results
        assertEquals(200, response.getStatusCodeValue()); // Verify status code is 200 OK
        assertEquals(mockTestList.size(), response.getBody().size()); // Verify the number of tests returned
        assertEquals("Foundation Test", response.getBody().get(0).getTestName()); // Verify content of the response
    }
    @Test
    void testStoreAnswer_InvalidToken() {
        // Given an invalid JWT token
        String invalidToken = "Bearer invalid-jwt-token";
        StoreAnswer storeAnswerDTO = new StoreAnswer();

        // Mocking the JwtUtil behavior to throw an exception for invalid token
        when(jwtUtil.extractUserId("invalid-jwt-token")).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert - Verify exception handling
        assertThrows(RuntimeException.class, () -> {
            testController.storeAnswer(storeAnswerDTO, invalidToken);
        });

        // Verify that the extractUserId method was called
        verify(jwtUtil, times(1)).extractUserId("invalid-jwt-token");

        // Verify that storeAnswer was never called due to invalid token
        verify(answerService, never()).storeAnswer(anyLong(), anyLong(), anyLong(), anyString());
    }
}
