package com.globalitgeeks.examninja.service;

import com.globalitgeeks.examninja.dto.TestDto;
import com.globalitgeeks.examninja.exception.UserNotFoundException;
import com.globalitgeeks.examninja.model.AttemptTable;
import com.globalitgeeks.examninja.model.ScoreTable;
import com.globalitgeeks.examninja.model.TestTable;
import com.globalitgeeks.examninja.model.User;
import com.globalitgeeks.examninja.repository.AttemptRepository;
import com.globalitgeeks.examninja.repository.ScoreRepository;
import com.globalitgeeks.examninja.repository.TestRepository;
import com.globalitgeeks.examninja.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TestDto> getTestsForUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {

        // Get all available tests
        List<TestTable> allTests = testRepository.findAll();
        List<TestDto> testDtos = new ArrayList<>();

        for (TestTable test : allTests) {
            // Find attempts for the test by the user
            List<AttemptTable> attempts = attemptRepository.findByUserId(userId)
                    .stream()
                    .filter(attempt -> attempt.getTestId().equals(test.getTestId()))
                    .collect(Collectors.toList());

            Long totalAttempts = (long) attempts.size();
            Double totalScore = 0.0;

            // Calculate the total score for the user's attempts on this test
            for (AttemptTable attempt : attempts) {
                List<ScoreTable> scores = scoreRepository.findByAttemptId(attempt.getAttemptId());
                for (ScoreTable score : scores) {
                    totalScore += score.getScore();
                }
            }

            // Calculate the average score
            Double averageScore = totalAttempts > 0 ? totalScore / totalAttempts : 0.0;

            // Create the DTO
            TestDto dto = new TestDto(test.getTestId(), test.getTestName(), totalAttempts, averageScore);
            testDtos.add(dto);
        }
            return testDtos;
        } else {
            throw new UserNotFoundException("User not found");
        }


    }
}
