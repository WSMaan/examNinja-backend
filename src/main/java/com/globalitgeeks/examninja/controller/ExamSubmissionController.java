package com.globalitgeeks.examninja.controller;


import com.globalitgeeks.examninja.dto.ExamResultResponse;
import com.globalitgeeks.examninja.dto.ExamSubmissionRequest;
import com.globalitgeeks.examninja.exception.InvalidExamRequestException;
import com.globalitgeeks.examninja.service.AnswerService;
import com.globalitgeeks.examninja.service.ExamResultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
        public ResponseEntity<ExamResultResponse> submitTest(@Valid @RequestBody ExamSubmissionRequest request) {

            if (request.getTestId() == null || request.getId()== null || request.getAnswers() == null || request.getAnswers().isEmpty()) {
                throw new InvalidExamRequestException("Request is missing required fields: testId, id, or answers.");
            }

            ExamResultResponse response = examResultService.processSubmittedTest(request);

       return ResponseEntity.ok()
               .contentType(MediaType.APPLICATION_JSON)
               .body(response);
        }

}



