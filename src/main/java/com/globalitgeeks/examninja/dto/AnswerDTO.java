package com.globalitgeeks.examninja.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {

        private Long questionId;
        private String selectedOption;

        // Getters and setters

}
