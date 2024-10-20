package com.globalitgeeks.examninja.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AnswerService {

    private Map<String, String> answerMap = new HashMap<>();

    public void storeAnswer(Integer studentId, Integer testId, Integer questionId, Integer questionNumber, String selectedOption) {
        String key = generateKey(studentId, testId, questionId, questionNumber);
        if (answerMap.containsKey(key)) {
            //Updates the existing answer
            answerMap.put(key, selectedOption);
            System.out.println("Answer updated for Question ID: " + questionId);
        } else {
            //Inserts a new answer
            answerMap.put(key, selectedOption);
            System.out.println("New Answer added for  Question ID: " + questionId);
        }

    //    for (Map.Entry<String, String> entry : answerMap.entrySet()) {
    //        System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
    //    }
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
