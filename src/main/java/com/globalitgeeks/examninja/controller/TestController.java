package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {














    @Autowired
    private TestService testService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<TestDto>> getTestsForUser(@PathVariable Long userId) {
        List<TestDto> testList = testService.getTestsForUser(userId);
        return ResponseEntity.ok(testList);
    }
}
