package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.ExamDataBaseOperationException;
import com.globalitgeeks.examninja.exception.InvalidExamDataException;
import com.globalitgeeks.examninja.exception.QuestionNotFoundException;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamResultServiceTest {

    @InjectMocks
    private ExamResultService examResultService;

    @Mock
    private AnswerService answerService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ExamResultDetailRepository examResultDetailRepository;

    @Mock
    private ExamResultRepository examResultRepository;

    private ExamSubmissionRequest request;
    private Long userId;
    private LocalDateTime submissionDateTime;

    @BeforeEach
    public void setUp() {
        request = new ExamSubmissionRequest();
        request.setTestId(1L);
        userId = 1L;
        submissionDateTime = LocalDateTime.now();
    }

    @Test
    public void testProcessSubmittedTest_NoAnswers() {


        assertThrows(InvalidExamDataException.class, () -> examResultService.processSubmittedTest(request, userId));
    }

    @Test
    public void testProcessSubmittedTest_QuestionNotFound() {
        Map<String, Map<String, String>> allAnswers = new HashMap<>();
        Map<String, String> answers = new HashMap<>();
        answers.put("label", "A");
        allAnswers.put("1-1-1", answers);
        assertThrows(InvalidExamDataException.class, () -> examResultService.processSubmittedTest(request, userId));
    }

    @Test
    public void testProcessSubmittedTest_SaveExamResultException() {
        Map<String, Map<String, String>> allAnswers = new HashMap<>();
        Map<String, String> answers = new HashMap<>();
        answers.put("label", "A");
        allAnswers.put("1-1-1", answers);


        Question question = new Question();
        question.setQuestionId(1L);
        question.setCorrectAnswer("A");
        assertThrows(InvalidExamDataException.class, () -> examResultService.processSubmittedTest(request, userId));
    }
}
