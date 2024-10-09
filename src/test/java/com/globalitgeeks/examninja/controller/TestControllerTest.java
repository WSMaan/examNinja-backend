package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;
    @Mock
    private TestService testService;

    @InjectMocks
    private TestController testController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void getQuestionByTestId_ReturnsQuestionPage() throws Exception {
        // Given
        Long testId = 1L;
        int page = 0;
        Question question = new Question(); // Create a mock question object
        Page<Question> questionPage = new PageImpl<>(Collections.singletonList(question), PageRequest.of(page, 1), 1);

        // When
        when(questionService.getQuestionByTestId(testId, page, 1)).thenReturn(questionPage);

        // Then
        mockMvc.perform(get("/api/tests/{testId}/questions", testId)
                        .param("page", String.valueOf(page))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(questionService, times(1)).getQuestionByTestId(testId, page, 1);
    }

    @Test
    public void testGetTestsForUser() {
        // Arrange: Create mock data for the TestDto list
        List<TestDto> mockTestList = Arrays.asList(
                new TestDto(1L, "Foundation Test", 50L),
                new TestDto(2L, "Advanced Test", 30L)
        );

        // Mock the behavior of testService
        when(testService.getTestsForUser(1L)).thenReturn(mockTestList);

        // Act: Call the controller method
        ResponseEntity<List<TestDto>> response = testController.getTestsForUser(1L);

        // Assert: Verify the results
        assertEquals(200, response.getStatusCodeValue()); // Verify status code is 200 OK
        assertEquals(mockTestList.size(), response.getBody().size()); // Verify the number of tests returned
        assertEquals("Foundation Test", response.getBody().get(0).getTestName()); // Verify content of the response
    }
}
