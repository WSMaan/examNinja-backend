package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page) {

        // Delegating logic to the QuestionService
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, 1);

        return ResponseEntity.ok(response);
    }
}



