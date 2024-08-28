package org.example.final_project.service;

import org.example.final_project.dto.request.StudentScoreRequest;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.StudentScoreResponse;

/**
 * Interface for managing student scores.
 */
public interface StudentScoreService {

    /**
     * Retrieves the score of a student by their ID.
     *
     * @param studentId the ID of the student.
     * @return a {@link ResponseData} containing the {@link StudentScoreResponse}.
     */
    ResponseData<StudentScoreResponse> viewStudentScore(String studentId);

    /**
     * Inserts or updates a student's score.
     *
     * @param request the details of the student's score.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> insertStudentScore(StudentScoreRequest request);
}
