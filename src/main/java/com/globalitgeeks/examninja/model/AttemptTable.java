package com.globalitgeeks.examninja.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attempt_table")
public class AttemptTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    private Long testId;  // Foreign key from TestTable
    private Long userId;  // Foreign key from UserTable

    // Constructors, Getters, Setters
}
