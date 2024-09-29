package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.ScoreTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<ScoreTable, Long> {
    List<ScoreTable> findByAttemptId(Long attemptId);
}
