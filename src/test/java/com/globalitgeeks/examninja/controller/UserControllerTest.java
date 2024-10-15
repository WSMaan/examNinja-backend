package com.globalitgeeks.examninja.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globalitgeeks.examninja.dto.*;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

    }

    @Test
    public void testRegister() {
        // Arrange
        UserRegisterRequest registerRequest = new UserRegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password@123");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password@123");

        when(userService.register(any(UserRegisterRequest.class))).thenReturn(user);

        // Act
        ResponseEntity<ApiResponse> responseEntity = userController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("User Registered Successfully!", responseEntity.getBody().getMessage());
        verify(userService, times(1)).register(any(UserRegisterRequest.class));
    }
    // Test for User Login
    @Test
    void testLogin() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password@123");

        String token = "some.jwt.token";  // Example token to be returned
        when(userService.login(any(UserRequest.class))).thenReturn(token);

        // Act
        ResponseEntity<?> response = userController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Logged in Successfully!", ((ApiResponse) response.getBody()).getMessage());
        assertEquals(token, ((ApiResponse) response.getBody()).getToken()); // Check if the token is included in the response
        verify(userService, times(1)).login(any(UserRequest.class));
    }


    // Test for Change Password
    @Test
    void testChangePassword() {
        // Arrange
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("john.doe@example.com");
        changePasswordRequest.setOldPassword("newpassword@123");
        changePasswordRequest.setOldPassword("examninja@123");


        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("newpassword@123");
        mockUser.setPassword("examninja@123");

        when(userService.changePassword(any(ChangePasswordRequest.class))).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = userController.changePassword(changePasswordRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully", ((ApiResponse) response.getBody()).getMessage());
        verify(userService, times(1)).changePassword(any(ChangePasswordRequest.class));
    }
    @Test
    void testResetPassword_Success() throws Exception {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("john.doe@example.com");

        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("newRandomPassword@123");

        when(userService.resetPassword(any(ResetPasswordRequest.class))).thenReturn(mockUser);

        // Act
        ResponseEntity<?> response = userController.resetPassword(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String expectedMessage = "Password reset successfully. New Password is: newRandomPassword@123";
        assertEquals(expectedMessage, ((ApiResponse) response.getBody()).getMessage());
        verify(userService, times(1)).resetPassword(any(ResetPasswordRequest.class));
    }
}
