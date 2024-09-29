package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.AttemptTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttemptRepository extends JpaRepository<AttemptTable, Long> {
    List<AttemptTable> findByUserId(Long userId);
}
