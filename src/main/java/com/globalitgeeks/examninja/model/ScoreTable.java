package com.globalitgeeks.examninja.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "score_table")
public class ScoreTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scoreId;

    private Long attemptId;  // Foreign key from AttemptTable
    private Double score;    // The score for the specific attempt


}
