package com.globalitgeeks.examninja.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "test")
@Data
@Setter
@Getter
public class TestTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testId;

    private String testName;
    private Integer numberOfQuestions;

    // Mapping with questions; ignored in JSON to prevent stack overflow
    @OneToMany(mappedBy = "testTable")
    @JsonIgnore
    private List<Question> questions;
}