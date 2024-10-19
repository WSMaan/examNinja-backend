package com.globalitgeeks.examninja.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerStorageService {


    private  Map<String, String> answerMap = new HashMap<>();

    // Method to store the answers in the HashMap
    public void storeAnswer(Long studentId, Long testId, Long questionId, Long questionNumber, String selectedOption) {
        String key = generateKey(studentId, testId, questionId, questionNumber);
        answerMap.put(key, selectedOption);
    }

    // Method to generate a unique key (studentId-testId-questionId-questionNumber)
    private String generateKey(Long studentId, Long testId, Long questionId, Long questionNumber) {
        return studentId + "-" + testId + "-" + questionId + "-" + questionNumber;
    }

    // Method to retrieve all stored answers
    public Map<String, String> getAllAnswers() {
        return new HashMap<>(answerMap); // Return a copy of the answerMap
    }
}



