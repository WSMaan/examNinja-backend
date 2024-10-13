package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TestService testService;

    @Autowired
    private JwtUtil jwtUtil; // Inject your JWT utility class

    @GetMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page,
            @RequestHeader("Authorization") String token) { // Accept JWT token

        Long userId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // Extract user ID from token

        // Delegating logic to the QuestionService
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, 1);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    public ResponseEntity<List<TestDto>> getTestsForUser(
            @RequestHeader("Authorization") String token) { // Accept JWT token

        Long extractedUserId = jwtUtil.extractUserId(token.replace("Bearer ", "")); // Extract user ID from token

        List<TestDto> testList = testService.getTestsForUser(extractedUserId); // Use extracted user ID
        return ResponseEntity.ok(testList);
    }
}
