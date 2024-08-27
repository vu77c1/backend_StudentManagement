package org.example.final_project.repository;

import org.example.final_project.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Enrollment findByStudentStudentId(String studentId);

    Optional<List<Enrollment>> findByTeacherTeacherId(String teacherId);

    @Query("SELECT e FROM Enrollment e WHERE e.enrollmentId NOT IN (SELECT s.enrollment.enrollmentId FROM StudentScore s) AND e.teacher.teacherId = :teacherId")
    Optional<List<Enrollment>> findEnrollmentsWithoutScores(@Param("teacherId") String teacherId);

}