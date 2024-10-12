package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.UserRegisterRequest;
import com.globalitgeeks.examninja.dto.UserRequest;
import com.globalitgeeks.examninja.exception.InvalidPasswordException;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.UserRepository;
import com.globalitgeeks.examninja.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case for registering a user
    @Test
    void testRegisterSuccess() {
        // Given
        UserRegisterRequest request = new UserRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User savedUser = userService.register(request);

        // Then
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test case for successful login
    @Test
    void testLogin_Successful() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password@123");

        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("password@123");
        mockUser.setId(1L); // Set a sample user ID

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(mockUser.getEmail(), mockUser.getId())).thenReturn("some.jwt.token");

        // Act
        String token = userService.login(loginRequest);

        // Assert
        assertEquals("some.jwt.token", token); // Verify the token returned
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, times(1)).generateToken(mockUser.getEmail(), mockUser.getId());
    }

    @Test
    void testLogin_UserNotFound() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password@123");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyLong()); // Ensure token generation is not called
    }

    @Test
    void testLogin_InvalidPassword() {
        // Arrange
        UserRequest loginRequest = new UserRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("wrongpassword");

        User mockUser = new User();
        mockUser.setEmail("john.doe@example.com");
        mockUser.setPassword("password@123"); // Correct password
        mockUser.setId(1L); // Set a sample user ID

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(mockUser));

        // Act & Assert
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(InvalidPasswordException.class, () -> {
            userService.login(loginRequest);
        });

        assertEquals("Incorrect password", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(any(String.class));
        verify(jwtUtil, never()).generateToken(anyString(), anyLong()); // Ensure token generation is not called
    }

    // Test case for changing password successfully
    @Test
    void testChangePasswordSuccess() {
        // Given
        UserRequest request = new UserRequest();
        request.setEmail("john@example.com");
        request.setPassword("newpassword");

        User user = new User();
        user.setEmail("john@example.com");
        user.setPassword("oldpassword");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User updatedUser = userService.changePassword(request);

        // Then
        assertNotNull(updatedUser);
        assertEquals("newpassword", updatedUser.getPassword());
        verify(userRepository, times(1)).findByEmail("john@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    // Test case for changing password (User Not Found)
    @Test
    void testChangePasswordUserNotFound() {
        // Given
        UserRequest request = new UserRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword("newpassword");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.changePassword(request));
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }
}
