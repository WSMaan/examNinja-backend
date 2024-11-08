package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.ExamResultDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ExamResultDetailRepositoryTest {

    // Mocking the ExamResultDetailRepository
    @Mock
    private ExamResultDetailRepository examResultDetailRepository;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
    }

    // Test method for findByTestId
    @Test
    void testFindByTestId_ShouldReturnList_WhenTestIdExists() {
        // Arrange
        Long testId = 1L;
        LocalDateTime submissionDateTime = LocalDateTime.now();
        List<ExamResultDetail> examResultDetails = Arrays.asList(
                new ExamResultDetail(1L, 1L, 1L, 1L, "A", "B", submissionDateTime),
                new ExamResultDetail(2L, 1L, 2L, 2L, "B", "C",submissionDateTime)
        );
        // Mock the repository behavior
        when(examResultDetailRepository.findByTestId(testId)).thenReturn(examResultDetails);

        // Act
        List<ExamResultDetail> result = examResultDetailRepository.findByTestId(testId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getSubmittedAnswer());
    }
    @Test
    void testFindById_ShouldReturnOptional_WhenUserIdExists() {
        // Arrange
        Long userId = 1L;
        LocalDateTime submissionDateTime = LocalDateTime.now();
        ExamResultDetail examResultDetail = new ExamResultDetail(1L, 1L, userId, 1L, "A", "B", submissionDateTime);
        // Mock the repository behavior
        when(examResultDetailRepository.findById(userId)).thenReturn(Optional.of(examResultDetail));

        // Act
        Optional<ExamResultDetail> result = examResultDetailRepository.findById(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(submissionDateTime, result.get().getSubmissionDateTime());
    }

    @Test
    void testFindByQuestionId_ShouldReturnList_WhenQuestionIdExists() {
        // Arrange
        Long questionId = 1L;
        LocalDateTime submissionDateTime = LocalDateTime.now();
        List<ExamResultDetail> examResultDetails = Arrays.asList(
                new ExamResultDetail(1L, 1L, 1L, questionId, "A", "B", submissionDateTime),
                new ExamResultDetail(2L, 1L, 2L, questionId, "C", "D", submissionDateTime)
        );
        // Mock the repository behavior
        when(examResultDetailRepository.findByQuestionId(questionId)).thenReturn(examResultDetails);

        // Act
        List<ExamResultDetail> result = examResultDetailRepository.findByQuestionId(questionId);

        // Assert
        assertEquals(2, result.size());
        assertEquals(submissionDateTime, result.get(0).getSubmissionDateTime());
    }
}
