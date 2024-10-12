package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.TestTable;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import com.globalitgeeks.examninja.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TestRepository testRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    public void testGetTestsForUser_Success() {
        // Arrange: Set up mock user, test, and question data
        User mockUser = new User();
        mockUser.setId(1L);

        TestTable mockTest1 = new TestTable();
        mockTest1.setTestId(1L);
        mockTest1.setTestName("Foundation Test");

        TestTable mockTest2 = new TestTable();
        mockTest2.setTestId(2L);
        mockTest2.setTestName("Advanced Test");

        List<TestTable> mockTestList = Arrays.asList(mockTest1, mockTest2);

        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(testRepository.findAll()).thenReturn(mockTestList);
        when(questionRepository.countByTestId(1L)).thenReturn(50L);
        when(questionRepository.countByTestId(2L)).thenReturn(30L);

        // Act: Call the service method
        List<TestDto> result = testService.getTestsForUser(1L);

        // Assert: Check the returned values
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Foundation Test", result.get(0).getTestName());
        assertEquals(50L, result.get(0).getNumberOfQuestions());
        assertEquals("Advanced Test", result.get(1).getTestName());
        assertEquals(30L, result.get(1).getNumberOfQuestions());
    }

    @Test
    public void testGetTestsForUser_UserNotFound() {
        // Arrange: Mock user not found
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Check if the UserNotFoundException is thrown
        assertThrows(UserNotFoundException.class, () -> {
            testService.getTestsForUser(1L);
        });
    }
}
