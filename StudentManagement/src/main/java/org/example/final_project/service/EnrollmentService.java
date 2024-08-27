package org.example.final_project.service;

import org.example.final_project.dto.request.CreateEnrollmentDto;
import org.example.final_project.dto.request.UpdateEnrollmentDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;

public interface EnrollmentService {
    PageResponse<?> getAllEnrollments(int pageNo, int pageSize, String sortBy);

    ResponseData<?> getEnrollmentById(Long enrollmentId);

    ResponseData<?> createEnrollment(CreateEnrollmentDto enrollmentDto);

    ResponseData<?> updateEnrollment(Long enrollmentId, UpdateEnrollmentDto enrollmentDto);

    void deleteEnrollment(Long enrollmentId);

    ResponseData<?> getEnrollmentByTeacherId(String teacherId);
}
