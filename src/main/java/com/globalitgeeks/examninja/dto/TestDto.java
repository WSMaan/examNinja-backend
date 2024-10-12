package com.globalitgeeks.examninja.dto;

import lombok.Data;

@Data
public class TestDto {

    private Long testId;
    private String testName;

    private Long numberOfQuestions;

    public TestDto(Long testId, String testName,Long numberOfQuestions) {
        this.testId = testId;
        this.testName = testName;
        this.numberOfQuestions = numberOfQuestions;

    }


}
