package com.globalitgeeks.examninja.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*@Data
@NoArgsConstructor
@AllArgsConstructor*/
@Entity
@Data
@Table(name = "exam_result_details")
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultDetail {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long resultDetailId;  // Unique identifier for each exam result detail
        @Column(name = "user_id", nullable = false)
        private Long id;  // Identifier for the user/student
        @Column(name = "test_id", nullable = false)
        private Long testId;  // Identifier for the exam
        @Column(name = "question_id", nullable = false)
        private Long questionId;  // Identifier for the question
        @Column(name = "selected_option", nullable = false)
        private String submittedAnswer;  // The student's submitted answer for the question
        @Column(name = "correct_answer", nullable = false)  // Field to store the correct answer
        private String correctAnswer;  // The correct answer for the question
        @Column(name = "date_time", nullable = false)
        private LocalDateTime submissionDateTime;



    }


