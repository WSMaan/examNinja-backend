package com.globalitgeeks.examninja.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AnswerService {

    public static Map<String, Map<String, String>> answerMap = new HashMap<>();
    // Method to store answer selected by user in the Hash Map
    public void storeAnswer(Long userId, Long testId, Long questionId, Map <String, String> selectedOption) {
        String key = generateKey(userId, testId, questionId);
        if (answerMap.containsKey(key)) {
            //Updates the existing answer
            answerMap.put(key, selectedOption);

        } else {
            //Inserts a new answer
            answerMap.put(key, selectedOption);
        }
    }

    // Method to generate the unique key (studentId-questionId)
    private static String generateKey(Long userId, Long testId, Long questionId) {
        return userId + "-" +testId + "-" + questionId;
    }

    public static Map<String, Map<String, String>> getAllAnswers() {
        // Return a copy of the map
        return new HashMap<>(answerMap);
    }

    public static Map<String, String> getAnswer(Long userId, Long testId, Long questionId) {
        return answerMap.get(generateKey(userId, testId, questionId));
    }

}
