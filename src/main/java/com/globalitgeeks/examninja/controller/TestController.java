package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private QuestionService questionService;

    @GetMapping ("/{testId}/questions")
    public ResponseEntity<Page<Question>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page) { // `size` is fixed to 1 since we fetch one question at a time

        // Fetch one question at a time based on the page number
        Page<Question> question = questionService.getQuestionByTestId(testId, page, 1);
        return ResponseEntity.ok(question);
    }
}