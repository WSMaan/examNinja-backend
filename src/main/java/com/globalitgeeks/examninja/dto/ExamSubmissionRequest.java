package com.globalitgeeks.examninja.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionRequest {
    private Long testId;
    private Long Id;
    private List<AnswerDTO> answers;
}

