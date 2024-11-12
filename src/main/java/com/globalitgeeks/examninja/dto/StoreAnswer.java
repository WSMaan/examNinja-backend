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

    private Long testId;
    private Long questionId;
    private Option selectedOption;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Option {
        private String value;
        private String label;
    }
}



