package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.Question;
import com.globalitgeeks.examninja.model.TestTable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Fetch questions by level
    List<Question> findByLevel(String level);

    // Fetch questions associated with the given test and handle pagination
    Page<Question> findByTestTable(TestTable testTable, Pageable pageable); // Updated to use testTable
}