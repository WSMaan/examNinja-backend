package com.globalitgeeks.examninja.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataOutput;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam_results")
public class ExamResult {
    @Id  // Add this annotation to designate the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "result_id")// Optional, but recommended for auto-incrementing IDs
    private Long resultId;  // Unique identifier for each exam result

    @Column(name = "test_id",nullable = false)
    private Long testId;


    @Column(name = "user_id", nullable = false)
    private Long id;              // Identifier for the user/student

    @Column(name = "score", nullable = false)
    private double score;        // Percentage score for the exam

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "Date_Time", nullable = false)
    private LocalDateTime submissionDateTime;

}

