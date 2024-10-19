package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.service.AnswerStorageService;
import com.globalitgeeks.examninja.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
public class ExamSubmissionController {
    @Autowired
    private ExamResultService examResultService;

    @PostMapping("/submit")
    public ResponseEntity<ExamResultResponse> submitTest(@RequestBody ExamSubmissionRequest request) {
        // Call service to process the submitted test
        ExamResultResponse response = examResultService.processSubmittedTest(request);

        // Return the response with HTTP status OK (200)
        return ResponseEntity.ok(response);
    }
}


