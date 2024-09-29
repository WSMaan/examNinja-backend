package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.model.TestTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class QuestionRepositoryTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private TestTable testTable;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testTable = new TestTable();
        testTable.setTestId(1L);
    }

    @Test
    public void testFindByTest() {
        // Arrange
        Question question = new Question();
        question.setQuestionId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Question> page = new PageImpl<>(Collections.singletonList(question));

        // Mock the behavior of findByTest
        when(questionRepository.findByTestTable(testTable, pageable)).thenReturn(page);

        // Act
        Page<Question> result = questionRepository.findByTestTable(testTable, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getQuestionId());
    }

    @Test
    public void testFindByLevel() {
        // Arrange
        Question question1 = new Question();
        question1.setQuestionId(1L);
        question1.setLevel("Easy");

        Question question2 = new Question();
        question2.setQuestionId(2L);
        question2.setLevel("Easy");

        List<Question> questionList = Arrays.asList(question1, question2);

        // Mock the behavior of findByLevel
        when(questionRepository.findByLevel("Easy")).thenReturn(questionList);

        // Act
        List<Question> result = questionRepository.findByLevel("Easy");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Easy", result.get(0).getLevel());
        assertEquals("Easy", result.get(1).getLevel());
    }
}

