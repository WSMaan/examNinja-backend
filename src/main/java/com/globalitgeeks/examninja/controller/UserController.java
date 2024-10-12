package com.globalitgeeks.examninja.controller;

import com.globalitgeeks.examninja.dto.ApiResponse;
import com.globalitgeeks.examninja.dto.UserRegisterRequest;
import com.globalitgeeks.examninja.dto.UserRequest;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
        userService.register(registerRequest);
        ApiResponse response = new ApiResponse("success", "User Registered Successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserRequest request) {
        String token = userService.login(request); // Get token
        ApiResponse response = new ApiResponse("success", "User Logged in Successfully!");
        response.setToken(token); // Set token in response
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Change Password Endpoint
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserRequest request) {
        User updatedUser = userService.changePassword(request);
        ApiResponse response = new ApiResponse("success", "Password changed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

