package com.globalitgeeks.examninja.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "Email is mandatory")
    @NotEmpty(message = "Email field can't be empty")
    @Email(message = "Invalid email format")
    private String email;
}
