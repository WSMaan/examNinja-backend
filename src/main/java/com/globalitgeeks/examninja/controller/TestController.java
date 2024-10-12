package com.globalitgeeks.examninja.controller;
import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.model.Question;
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


    @GetMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionByTestId(
            @PathVariable Long testId,
            @RequestParam(defaultValue = "0") int page) {

        // Delegating logic to the QuestionService
        Map<String, Object> response = questionService.getQuestionByTestId(testId, page, 1);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<TestDto>> getTestsForUser(@PathVariable Long userId) {
        List<TestDto> testList = testService.getTestsForUser(userId);
        return ResponseEntity.ok(testList);
    }
}



