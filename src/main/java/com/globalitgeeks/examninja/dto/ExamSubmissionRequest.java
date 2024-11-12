package com.globalitgeeks.examninja.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamSubmissionRequest {
    @NotNull(message = "Test ID cannot be null")
    private Long testId;
    @NotNull(message = "LocalDateTime cannot be null")
    private LocalDateTime submissionDateTime;

}

