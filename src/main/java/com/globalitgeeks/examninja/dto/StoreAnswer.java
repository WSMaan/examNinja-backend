package com.globalitgeeks.examninja.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreAnswer {

    private Long testId;
    private Long questionId;
    private Map<String, String> selectedOption; // Change Option to Map

}