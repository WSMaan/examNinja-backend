package com.globalitgeeks.examninja.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.globalitgeeks.examninja.service.AnswerService;

import java.util.Map;

class AnswerServiceTest {

    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        // Create a new instance of the AnswerService for testing
        answerService = new AnswerService();
    }

    @Test
    void testStoreAnswer_NewAnswer() {
        // Given
        Long userId = 1L;
        Long testId = 101L;
        Long questionId = 1001L;
        String selectedOption = "Option A";

        // Act - Store a new answer
        answerService.storeAnswer(userId, testId, questionId, selectedOption);

        // Assert - Verify that the answer was stored correctly
        String storedAnswer = AnswerService.getAnswer(userId, testId, questionId);
        assertNotNull(storedAnswer);
        assertEquals(selectedOption, storedAnswer);
    }

    @Test
    void testStoreAnswer_UpdateExistingAnswer() {
        // Given
        Long userId = 2L;
        Long testId = 102L;
        Long questionId = 1002L;
        String initialOption = "Option B";
        String updatedOption = "Option C";

        // Act - Store an initial answer
        answerService.storeAnswer(userId, testId, questionId, initialOption);
        // Act - Update the existing answer
        answerService.storeAnswer(userId, testId, questionId, updatedOption);

        // Assert - Verify that the answer was updated correctly
        String storedAnswer = AnswerService.getAnswer(userId, testId, questionId);
        assertNotNull(storedAnswer);
        assertEquals(updatedOption, storedAnswer);
    }

    @Test
    void testGetAllAnswers() {
        // Given
        Long userId1 = 1L;
        Long testId1 = 201L;
        Long questionId1 = 2001L;
        String selectedOption1 = "Option D";

        Long userId2 = 2L;
        Long testId2 = 202L;
        Long questionId2 = 2002L;
        String selectedOption2 = "Option E";

        // Act - Store multiple answers
        answerService.storeAnswer(userId1, testId1, questionId1, selectedOption1);
        answerService.storeAnswer(userId2, testId2, questionId2, selectedOption2);

        // Act - Retrieve all stored answers
        Map<String, String> allAnswers = AnswerService.getAllAnswers();

        // Assert - Verify that the answers are retrieved correctly
        assertEquals(4, allAnswers.size());
        assertEquals(selectedOption1, allAnswers.get(userId1 + "-" + testId1 + "-" + questionId1));
        assertEquals(selectedOption2, allAnswers.get(userId2 + "-" + testId2 + "-" + questionId2));
    }

    @Test
    void testGetAnswer_NonExistent() {
        // Given non-existent data
        Long nonExistentUserId = 99L;
        Long nonExistentTestId = 999L;
        Long nonExistentQuestionId = 9999L;

        // Act - Attempt to retrieve a non-existent answer
        String answer = AnswerService.getAnswer(nonExistentUserId, nonExistentTestId, nonExistentQuestionId);

        // Assert - Verify that the answer is null
        assertNull(answer);
    }
}
