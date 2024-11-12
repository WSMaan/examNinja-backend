package com.globalitgeeks.examninja.repository;

import com.globalitgeeks.examninja.model.ExamResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultDetailRepository extends JpaRepository<ExamResultDetail, Long> {

    // Find by testId
    List<ExamResultDetail> findByTestId(Long testId);

    // Find by userId (or id in your entity)
    Optional<ExamResultDetail> findById(Long userId);

    // If you want to find results by questionId
    List<ExamResultDetail> findByQuestionId(Long questionId);

    // List<ExamResultDetail> findByTestIdAndUserId(Long testId, Long userId);
}


