package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestionByTestId() {
        Long testId = 1L;
        int page = 0;
        String token = "Bearer mock-jwt-token";  // Mock token for testing

        // Mocking the response from the QuestionService
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("questionNumber", "1 of 60");
        mockResponse.put("questions", new Object[] {}); // Mock question objects as needed

        // Mocking the service method
        when(questionService.getQuestionByTestId(testId, page, 1)).thenReturn(mockResponse);
        when(jwtUtil.extractUserId("mock-jwt-token")).thenReturn(1L);  // Mock user ID extraction


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
}
