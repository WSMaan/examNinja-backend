package com.globalitgeeks.examninja.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "questions")
@Data
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String correctAnswer;
    private String answerDescription;
    private String category;
    private String level; // "Easy", "Medium", "Hard"
    private String questionType;

    // Mapping the question to its respective test
    @ManyToOne()
    @JoinColumn(name = "test_id")
    private TestTable testTable;
}