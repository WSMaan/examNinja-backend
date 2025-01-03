package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {


    // Fetch questions associated with the given test id and handle pagination
    Page<Question> findByTestId(Long testId, Pageable pageable);

    // Updated to use testTable
    Long countByTestId(Long testId);
}
