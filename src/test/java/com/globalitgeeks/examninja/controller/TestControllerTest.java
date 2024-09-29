package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.service.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private QuestionService questionService;

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
}
