package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.TestTable;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.QuestionRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import com.globalitgeeks.examninja.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TestDto> getTestsForUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            // Get all available tests
            List<TestTable> allTests = testRepository.findAll();
            List<TestDto> testDtos = new ArrayList<>();

            for (TestTable test : allTests) {
                Long numberOfQuestions = questionRepository.countByTestId(test.getTestId());
                // Create the DTO with test name and number of questions
                TestDto dto = new TestDto(test.getTestId(), test.getTestName(), (long) numberOfQuestions.intValue());
                testDtos.add(dto);
            }

            return testDtos;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }
}
