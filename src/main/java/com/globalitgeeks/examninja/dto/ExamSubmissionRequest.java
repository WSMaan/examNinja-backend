package com.globalitgeeks.examninja.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionRequest {
    @NotNull(message = "Test ID cannot be null")
    private Long testId;
    @NotNull(message = "User ID cannot be null")
    private Long id;
    @NotEmpty(message = "Answers cannot be empty")
    private List<AnswerDTO> answers;
}

