package com.globalitgeeks.examninja.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExamResultResponse {
    private Long testId;
    private Long Id;
    private int score;
    private int passingScore;
    private String status;
}


