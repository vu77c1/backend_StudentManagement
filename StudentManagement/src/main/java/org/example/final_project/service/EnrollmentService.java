package org.example.final_project.service;

import org.example.final_project.dto.request.CreateEnrollmentDto;
import org.example.final_project.dto.request.UpdateEnrollmentDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;

/**
 * Interface for managing student enrollments.
 */
public interface EnrollmentService {

    /**
     * Retrieves all enrollments with pagination and sorting.
     *
     * @param pageNo   the page number.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @return a {@link PageResponse} containing the list of enrollments.
     */
    PageResponse<?> getAllEnrollments(int pageNo, int pageSize, String sortBy);

    /**
     * Retrieves an enrollment by its ID.
     *
     * @param enrollmentId the ID of the enrollment.
     * @return a {@link ResponseData} containing the enrollment details.
     */
    ResponseData<?> getEnrollmentById(Long enrollmentId);

    /**
     * Creates a new enrollment.
     *
     * @param enrollmentDto the enrollment details.
     * @return a {@link ResponseData} with the result of the creation.
     */
    ResponseData<?> createEnrollment(CreateEnrollmentDto enrollmentDto);

    /**
     * Updates an existing enrollment.
     *
     * @param enrollmentId  the ID of the enrollment to update.
     * @param enrollmentDto the updated enrollment details.
     * @return a {@link ResponseData} with the result of the update.
     */
    ResponseData<?> updateEnrollment(Long enrollmentId, UpdateEnrollmentDto enrollmentDto);

    /**
     * Deletes an enrollment by its ID.
     *
     * @param enrollmentId the ID of the enrollment to delete.
     */
    void deleteEnrollment(Long enrollmentId);

    /**
     * Retrieves enrollments by a teacher's ID.
     *
     * @param teacherId the ID of the teacher.
     * @return a {@link ResponseData} containing the list of enrollments.
     */
    ResponseData<?> getEnrollmentByTeacherId(String teacherId);
}
