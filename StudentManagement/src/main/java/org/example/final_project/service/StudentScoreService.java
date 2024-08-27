package org.example.final_project.service;

import org.example.final_project.dto.request.StudentScoreRequest;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.StudentScoreResponse;

public interface StudentScoreService {
    ResponseData<StudentScoreResponse> viewStudentScore(String studentId);

    ResponseData<?> insertStudentScore(StudentScoreRequest request);
}
