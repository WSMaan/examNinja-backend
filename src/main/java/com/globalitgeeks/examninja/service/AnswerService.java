package com.globalitgeeks.examninja.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AnswerService {

    private Map<String, String> answerMap = new HashMap<>();
    // Method to store answer selected by user in the Hash Map
    public void storeAnswer(Integer studentId, Integer testId, Integer questionId, Integer questionNumber, String selectedOption) {
        String key = generateKey(studentId, testId, questionId, questionNumber);
        if (answerMap.containsKey(key)) {
            //Updates the existing answer
            answerMap.put(key, selectedOption);

        } else {
            //Inserts a new answer
            answerMap.put(key, selectedOption);

        }


    }

    // Method to generate the unique key (studentId-questionId)
    private String generateKey(Integer studentId, Integer testId, Integer questionId, Integer questionNumber) {
        return studentId + "-" + testId + "-" + questionId + "-" + questionNumber;
    }

    public Map<String, String> getAllAnswers() {
        // Return a copy of the map
        return new HashMap<>(answerMap);
    }

    public String getAnswer(Integer studentId, Integer testId, Integer questionId, Integer questionNumber) {
        return answerMap.get(generateKey(studentId, testId, questionId, questionNumber));
    }

}
