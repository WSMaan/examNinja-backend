package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.*;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.ExamResultService;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.AnswerService;
import com.globalitgeeks.examninja.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
public class ExamController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestService testService;

    @Autowired
    private JwtUtil jwtUtil; // Inject your JWT utility class
    @Autowired
    private ExamResultService examResultService;

    @GetMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page,
            @RequestHeader("Authorization") String token) {

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // Extract user ID from token

        // Updated service call to include userId
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, 1, userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TestDto>> getTestsForUser(
            @RequestHeader("Authorization") String token) { // Accept JWT token
        Long extractedUserId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // Extract user ID from token

        List<TestDto> testList = testService.getTestsForUser(extractedUserId); // Use extracted user ID
        return ResponseEntity.ok(testList);
    }


    @Autowired
    private AnswerService answerService;

    @PostMapping("/save")
    public ResponseEntity<?> storeAnswer(@RequestBody StoreAnswer studentAnswerDTO, @RequestHeader("Authorization") String token) {
        Long extractedUserId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // Extract user ID from token
        Long testId = studentAnswerDTO.getTestId();
        Long questionId = studentAnswerDTO.getQuestionId();
        Map<String, String> selectedOption = studentAnswerDTO.getSelectedOption();

        answerService.storeAnswer(extractedUserId, testId, questionId, selectedOption);

        ApiResponse response = new ApiResponse("success", "Answer saved successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/submit")
    public ResponseEntity<ExamResultResponse> submitTest(
            @Valid @RequestBody ExamSubmissionRequest request,
            @RequestHeader("Authorization") String token) {


        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", ""));


        ExamResultResponse response = examResultService.processSubmittedTest(request, userId);

        return ResponseEntity.ok()
                .body(response);
    }


}


