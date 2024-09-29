package com.globalitgeeks.examninja.repository;


import com.globalitgeeks.examninja.model.TestTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestRepositoryTest {

    @Mock
    private TestRepository testRepository;

    @InjectMocks
    private TestTable testTable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTable = new TestTable();
        testTable.setTestId(1L);
        testTable.setTestName("Java Test");
    }

    @Test
    public void testResetAutoIncrement() {
        // Act
        testRepository.resetAutoIncrement();

        // Assert
        verify(testRepository).resetAutoIncrement();
    }

    // You can add more tests for custom query methods if needed
}