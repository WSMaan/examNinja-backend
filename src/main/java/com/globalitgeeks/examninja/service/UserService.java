package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.UserRegisterRequest;
import com.globalitgeeks.examninja.dto.UserRequest;
import com.globalitgeeks.examninja.exception.InvalidPasswordException;
import com.globalitgeeks.examninja.exception.UserAlreadyExistsException;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
    public User login(UserRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                return user;
            } else {
                throw new InvalidPasswordException("Incorrect password");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    // Change user password
    public User changePassword(UserRequest request) throws UserNotFoundException {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            User userPass = userOpt.get();
            userPass.setPassword(request.getPassword());
            return userRepository.save(userPass);
        } else {
            throw new UserNotFoundException("User not found with the provided email.");
        }
    }
}

