package com.globalitgeeks.examninja.service;


import com.globalitgeeks.examninja.ExamNinjaApplication;
import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.model.ExamResult;
import com.globalitgeeks.examninja.model.ExamResultDetail;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.repository.ExamResultDetailRepository;
import com.globalitgeeks.examninja.repository.ExamResultRepository;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ExamNinjaApplication.class) // Load Spring context with your main configuration class
public class ExamResultServiceTest {

    @InjectMocks
    private ExamResultService examResultService;

    @Mock
    private AnswerStorageService answerService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExamResultDetailRepository examResultDetailRepository;

    @Mock
    private ExamResultRepository examResultRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessSubmittedTest_CalculatesPercentageCorrectly() {
        // Arrange - Prepare the test data
        Long testId = 1L;
        Long userId = 1L;

        // Mock AnswerStorageService to return answers
        Map<String, String> answersMap = new HashMap<>();
        answersMap.put("1-1-101-1", "A"); // studentId-testId-questionId
        answersMap.put("1-1-102-2", "B");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        // Mock QuestionRepository to return correct answers
        Question question1 = new Question(101L, "Question 1", "A", "B", "C", "D", "A", null, null, null,null, testId);
        Question question2 = new Question(102L, "Question 2", "A", "B", "C", "D", "B", null, null, null, null,testId);

        when(questionRepository.findById(101L)).thenReturn(Optional.of(question1));
        when(questionRepository.findById(102L)).thenReturn(Optional.of(question2));

        // Act - Call the method to be tested
        ExamSubmissionRequest request = new ExamSubmissionRequest(testId, userId, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        // Assert - Verify the result
        assertEquals(100, response.getScore()); // 1 correct answer out of 2 = 50%
        assertEquals("PASS", response.getStatus());

        // Verify saving of the results to the repository
        Mockito.verify(examResultDetailRepository, Mockito.times(2)).save(Mockito.any(ExamResultDetail.class));
        Mockito.verify(examResultRepository, Mockito.times(1)).save(Mockito.any(ExamResult.class));
    }


    @Test
    public void testProcessSubmittedTest_HalfCorrectAnswers() {
        // Arrange
        Long testId = 1L;
        Long userId = 1L;

        // Mock AnswerStorageService to return correct answers for both questions
        Map<String, String> answersMap = new HashMap<>();
        answersMap.put("1-1-101-1", "A"); // studentId-testId-questionId
        answersMap.put("1-1-102-2", "C");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        // Mock QuestionRepository to return correct answers
        Question question1 = new Question(101L, "Question 1", "A", "B", "C", "D", "A", null, null, null, null,1L);
        Question question2 = new Question(102L, "Question 2", "A", "B", "C", "D", "B", null, null, null, null,1L);
        when(questionRepository.findById(101L)).thenReturn(Optional.of(question1));
        when(questionRepository.findById(102L)).thenReturn(Optional.of(question2));

        // Act
        ExamSubmissionRequest request = new ExamSubmissionRequest(testId, userId, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        // Assert
        assertEquals(50, response.getScore()); // 2 correct answers out of 2 = 100%
        assertEquals("FAIL", response.getStatus());
        System.out.println("exam failed got 50%");
    }
    @Test
    public void testProcessSubmittedTest_NoCorrectAnswers() {
        // Arrange - Prepare the test data
        Long testId = 1L;
        Long userId = 1L;

        // Mock AnswerStorageService to return wrong answers for both questions
        Map<String, String> answersMap = new HashMap<>();
        answersMap.put("1-1-101-1", "B"); // studentId-testId-questionId
        answersMap.put("1-1-102-2", "D");
        when(answerService.getAllAnswers()).thenReturn(answersMap);

        // Mock QuestionRepository to return correct answers (which are different from submitted ones)
        Question question1 = new Question(101L, "Question 1", "A", "B", "C", "D", "A", null, null, null,null, 1L); // Correct: "A"
        Question question2 = new Question(102L, "Question 2", "A", "B", "C", "D", "C", null, null, null,null, 1L); // Correct: "C"
        when(questionRepository.findById(101L)).thenReturn(Optional.of(question1));
        when(questionRepository.findById(102L)).thenReturn(Optional.of(question2));

        // Act - Call the method to be tested
        ExamSubmissionRequest request = new ExamSubmissionRequest(testId, userId, null);
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        // Assert - Verify the result
        assertEquals(0, response.getScore()); // 0 correct answers out of 2 = 0%
        assertEquals("FAIL", response.getStatus());

        // Verify that the results were saved to the repositories
        Mockito.verify(examResultDetailRepository, Mockito.times(2)).save(Mockito.any(ExamResultDetail.class));
        Mockito.verify(examResultRepository, Mockito.times(1)).save(Mockito.any(ExamResult.class));
    }




}
