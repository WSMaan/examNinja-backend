
package com.globalitgeeks.examninja.repository;

        import org.springframework.data.jpa.repository.JpaRepository;
        import com.globalitgeeks.examninja.model.ExamResult;
        import org.springframework.stereotype.Repository;

        import java.util.List;
        import java.util.Optional;


@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
        // JpaRepository provides built-in methods like save(), findById(), findAll(), deleteById()

        // Custom query example if needed
        //Optional<ExamResult> findById(Long userId);  // Find all exam results for a specific user
}

