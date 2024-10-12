package com.globalitgeeks.examninja.dto;

import lombok.Data;

@Data
public class ApiResponse {
    private String status;
    private String message;
    private String token; // Add token field

    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }


}
