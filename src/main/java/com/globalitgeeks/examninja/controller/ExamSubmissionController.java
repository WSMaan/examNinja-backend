package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.InvalidExamRequestException;
import com.globalitgeeks.examninja.security.JwtUtil;
import com.globalitgeeks.examninja.service.AnswerService;
import com.globalitgeeks.examninja.service.ExamResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
public class ExamSubmissionController {

   @Autowired
   private ExamResultService examResultService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/submit")
    public ResponseEntity<ExamResultResponse> submitTest(
            @Valid @RequestBody ExamSubmissionRequest request,
            @RequestHeader("Authorization") String token) {


        Long userId = jwtUtil.extractUserId(token);

        if (request.getTestId() == null ) {
           throw new InvalidExamRequestException("Request is missing required fields: testId or answers.");
        }

        ExamResultResponse response = examResultService.processSubmittedTest(request,userId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}



