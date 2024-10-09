package com.globalitgeeks.examninja.controller;

<<<<<<< HEAD
import com.globalitgeeks.examninja.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

=======
import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

>>>>>>> origin/ENJ-14
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



        // Fetch one question at a time based on the page number
        Page<Question> question = questionService.getQuestionByTestId(testId, page, 1);
        return ResponseEntity.ok(question);
    }
>>>>>>> origin/ENJ-14



