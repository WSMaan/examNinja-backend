package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.ChangePasswordRequest;
import com.globalitgeeks.examninja.dto.ResetPasswordRequest;
import com.globalitgeeks.examninja.dto.UserRegisterRequest;
import com.globalitgeeks.examninja.dto.UserRequest;
import com.globalitgeeks.examninja.exception.InvalidPasswordException;
import com.globalitgeeks.examninja.exception.UserAlreadyExistsException;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.UserRepository;
import com.globalitgeeks.examninja.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    // Register a new user
    public User register(UserRegisterRequest registerRequest) {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new UserAlreadyExistsException("User Already Exists with this Email!");
        }
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(registerRequest.getPassword());
        return userRepository.save(user);
    }

    // Login user
    public String login(UserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                // Generate token with userId
                return jwtUtil.generateToken(user.getEmail(), user.getId()); // Pass userId
            } else {
                throw new InvalidPasswordException("Incorrect password");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }


    // Change user password
    public User changePassword(ChangePasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()){
            User userPass = userOpt.get();
            // Check if the old password matches
            if (!request.getOldPassword().equals(userPass.getPassword())) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            userPass.setPassword(request.getNewPassword());
            return userRepository.save(userPass);
        }else {
            throw new UserNotFoundException("User not found with the provided email.");
        }
    }

    //Reset to random password
    public User resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("This Email is not registered"));

        // Generate a random password
        String newPassword = generateRandomPassword(10);

        // Encode and update the new password in the database
        user.setPassword(newPassword);
        return userRepository.save(user);

    }

    private String generateRandomPassword(int length) {
        final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final String SPECIAL_CHARS = "!@#$%^&*()";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        // Ensure at least one special character is included
        password.append(SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length())));

        // Generate the rest of the password
        for (int i = 1; i < length; i++) {
            password.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }

        return password.toString();
    }

}

