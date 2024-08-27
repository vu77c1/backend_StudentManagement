package org.example.final_project.repository;

import org.example.final_project.model.StudentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface StudentScoreRepository extends JpaRepository<StudentScore, Long> {
    List<StudentScore> findByEnrollmentStudentStudentId(String enrollmentStudentId);
}