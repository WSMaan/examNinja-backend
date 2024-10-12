package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @Mock
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetQuestionByTestId() {
        Long testId = 1L;
        int page = 0;

        // Mocking the response from the QuestionService
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("questionNumber", "1 of 60");
        mockResponse.put("questions", new Object[] {}); // mock question objects as needed

        when(questionService.getQuestionByTestId(testId, page, 1)).thenReturn(mockResponse);

        // Call the method to be tested
        ResponseEntity<Map<String, Object>> responseEntity = testController.getQuestionByTestId(testId, page);

        // Assertions
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockResponse, responseEntity.getBody());
    }
}
