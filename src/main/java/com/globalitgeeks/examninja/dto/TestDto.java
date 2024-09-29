package com.globalitgeeks.examninja.dto;

import lombok.Data;

@Data
public class TestDto {

    private Long testId;
    private String testName;
    private Long attempts;
    private Double averageScore;

    public TestDto(Long testId, String testName, Long attempts, Double averageScore) {
        this.testId = testId;
        this.testName = testName;
        this.attempts = attempts;
        this.averageScore = averageScore;
    }


}
