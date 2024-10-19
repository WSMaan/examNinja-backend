package com.globalitgeeks.examninja.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/*@Data
@NoArgsConstructor
@AllArgsConstructor*/
@Entity
@Table(name = "exam_result_details")


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

        // You can still map to the Question entity if you need to fetch the Question details
        /*@ManyToOne
        @JoinColumn(name = "question_id", referencedColumnName = "questionId", insertable = false, updatable = false)  // Mapping to the Question entity
        private Question question;  // Optional, if you want to access the Question entity directly*/

        // No-argument constructor
        public ExamResultDetail() {
        }

        // Parameterized constructor
        public ExamResultDetail(Long resultDetailId, Long testId, Long id, Long questionId, String submittedAnswer, String correctAnswer) {
                this.resultDetailId = resultDetailId;
                this.testId = testId;
                this.id = id;
                this.questionId = questionId;
                this.submittedAnswer = submittedAnswer;
                this.correctAnswer = correctAnswer;
        }

        // Getter and Setter methods
        public Long getResultDetailId() {
                return resultDetailId;
        }

        public void setResultDetailId(Long resultDetailId) {
                this.resultDetailId = resultDetailId;
        }

        public Long getTestId() {
                return testId;
        }

        public void setTestId(Long testId) {
                this.testId = testId;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getQuestionId() {
                return questionId;
        }

        public void setQuestionId(Long questionId) {
                this.questionId = questionId;
        }

        public String getSubmittedAnswer() {
                return submittedAnswer;
        }

        public void setSubmittedAnswer(String submittedAnswer) {
                this.submittedAnswer = submittedAnswer;
        }

        public String getCorrectAnswer() {
                return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer) {
                this.correctAnswer = correctAnswer;
        }
    }


