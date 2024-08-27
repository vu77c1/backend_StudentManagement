package org.example.final_project.repository;

import org.example.final_project.model.UniversityDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniversityDepartmentRepository extends JpaRepository<UniversityDepartment, UniversityDepartment.UniversityDepartmentId> {
    List<UniversityDepartment> findByIdUniversityId(String universityId);

}