package com.globalitgeeks.examninja.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StoreAnswer {

    private Integer studentId;
    private Integer testId;
    private Integer questionId;
    private Integer questionNumber;
    private String selectedOption;
    }




