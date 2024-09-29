package com.globalitgeeks.examninja.repository;


import com.globalitgeeks.examninja.model.TestTable;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<TestTable, Long> {
    // Custom query methods can be added here if needed
    // Custom query to reset auto-increment
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE test AUTO_INCREMENT = 1", nativeQuery = true)
    void resetAutoIncrement();
}
