package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.service.QuestionService;
import com.globalitgeeks.examninja.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private TestService testService;


    @GetMapping ("/{testId}/questions")
    public ResponseEntity<Page<Question>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page) { // `size` is fixed to 1 since we fetch one question at a time

        // Fetch one question at a time based on the page number
        Page<Question> question = questionService.getQuestionByTestId(testId, page, 1);
        return ResponseEntity.ok(question);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<TestDto>> getTestsForUser(@PathVariable Long userId) {
        List<TestDto> testList = testService.getTestsForUser(userId);
        return ResponseEntity.ok(testList);
    }




}