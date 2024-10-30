package com.globalitgeeks.examninja.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @NotEmpty(message = "New password can't be empty")
    @Size(min = 8, max = 15, message = "New password must be between 8 and 15 characters.")
    @Pattern(regexp = ".*[!@#$%^&*()_+{}|:<>?].*", message = "Password must contain at least 1 special character.")
    @Pattern(regexp = ".*[0-9].*", message = "Password must contain at least 1 number.")
    private String newPassword;
}