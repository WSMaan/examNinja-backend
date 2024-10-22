package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.ExamResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ExamResultRepositoryTest {

    @Mock
    private ExamResultRepository examResultRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    public void testFindById_ShouldReturnExamResult_WhenIdExists() {
        // Arrange
        Long resultId = 1L;
        ExamResult examResult = new ExamResult(resultId, 101L, 1001L, 85.5, "PASS");
        when(examResultRepository.findById(resultId)).thenReturn(Optional.of(examResult));

        // Act
        Optional<ExamResult> result = examResultRepository.findById(resultId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(examResult, result.get());
    }

    @Test
    public void testFindById_ShouldReturnEmptyOptional_WhenIdDoesNotExist() {
        // Arrange
        Long resultId = 999L;
        when(examResultRepository.findById(resultId)).thenReturn(Optional.empty());

        // Act
        Optional<ExamResult> result = examResultRepository.findById(resultId);

        // Assert
        assertTrue(result.isEmpty());  // Verify the result is an empty optional
    }

    @Test
    public void testSave_ShouldPersistExamResult() {
        // Arrange
        ExamResult examResult = new ExamResult(null, 101L, 1001L, 90.0, "FAIL");
        ExamResult savedExamResult = new ExamResult(1L, 101L, 1001L, 90.0, "FAIL");
        when(examResultRepository.save(examResult)).thenReturn(savedExamResult);

        // Act
        ExamResult result = examResultRepository.save(examResult);

        // Assert
        assertEquals(1L, result.getResultId());
        assertEquals("FAIL", result.getStatus());
        assertEquals(90.0, result.getScore());
    }
}
