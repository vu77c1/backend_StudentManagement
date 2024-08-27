package org.example.final_project.service.impl;

import org.example.final_project.dto.request.StudentScoreRequest;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.StudentScoreResponse;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.model.StudentScore;
import org.example.final_project.repository.EnrollmentRepository;
import org.example.final_project.repository.StudentScoreRepository;
import org.example.final_project.service.StudentScoreService;
import org.example.final_project.util.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentScoreServiceImpl implements StudentScoreService {

    @Autowired
    private StudentScoreRepository studentScoreRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;


    /**
     * View student scores
     *
     * @param studentId
     * @return
     */
    @Override
    public ResponseData<StudentScoreResponse> viewStudentScore(String studentId) {
        // Lấy danh sách điểm của sinh viên từ repository
        List<StudentScore> studentScores =
                studentScoreRepository.findByEnrollmentStudentStudentId(studentId);

        // Nếu không có điểm, trả về thông báo lỗi
        if (studentScores == null || studentScores.isEmpty()) {
            throw new ResourceNotFoundException("Student with ID " + studentId + " has no scores");
        }
        // Lấy thông tin sinh viên từ điểm đầu tiên (giả sử sinh viên chỉ có một thông tin)
        String fullName = studentScores.get(0).getEnrollment().getStudent().getFullName();
        String department =
                studentScores.get(0).getEnrollment().getStudent().getDepartment().getDepartmentName();

        // Tạo danh sách chi tiết điểm cho sinh viên
        List<StudentScoreResponse.StudentScoreDetail> studentScoreDetails =
                studentScores.stream().map(this::convertScoreToDetail).collect(Collectors.toList());

        // Tính toán GPA
        double gpa = calculateCGPA(studentScores);

        // Tính tổng số tín chỉ, chỉ tính những điểm có weighted average >= 4.0
        int totalCredits =
                studentScores.stream()
                        .filter(
                                score ->
                                        calculateWeightedAverage(
                                                score.getScoreTheory().doubleValue(),
                                                score.getScorePractical().doubleValue())
                                        >= 4.0)
                        .mapToInt(score -> score.getEnrollment().getCourse().getCredits())
                        .sum();

        // Tạo đối tượng StudentScoreResponse
        StudentScoreResponse studentScoreResponse =
                StudentScoreResponse.builder()
                        .studentId(studentId)
                        .fullName(fullName)
                        .department(department)
                        .studentScores(studentScoreDetails)
                        .totalCredits(totalCredits)
                        .gpa(gpa)
                        .build();

        return new ResponseData<>(
                HttpStatus.OK.value(), "Data retrieved successfully", studentScoreResponse);
    }

    @Override
    public ResponseData<?> insertStudentScore(StudentScoreRequest request) {
        if (!StringUtils.hasLength(String.valueOf(request.getEnrollmentId()))) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Enrollment ID is required", null);
        }

        // Find the enrollment by ID
        var enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found for ID: " + request.getEnrollmentId()));

        // Create a new StudentScore object
        StudentScore score = StudentScore.builder()
                .enrollment(enrollment)
                .scoreTheory(request.getScoreTheory())
                .scorePractical(request.getScorePractical())
                .build();

        // Save the score to the repository
        studentScoreRepository.save(score);

        // Return a success response
        return new ResponseData<>(HttpStatus.OK.value(), "Student score inserted successfully", null);
    }

    /**
     * @param score
     * @return
     */
    private StudentScoreResponse.StudentScoreDetail convertScoreToDetail(StudentScore score) {
        double theoryScore = score.getScoreTheory().doubleValue();
        double practicalScore = score.getScorePractical().doubleValue();
        double weightedAverage = calculateWeightedAverage(theoryScore, practicalScore);

        return StudentScoreResponse.StudentScoreDetail.builder()
                .subject(score.getEnrollment().getCourse().getName())
                .credits(score.getEnrollment().getCourse().getCredits())
                .theoryScore(theoryScore)
                .practicalScore(practicalScore)
                .weightedAverage(weightedAverage)
                .letterGrade(convertToGrade(weightedAverage))
                .grade4(calculateGrade4(weightedAverage))
                .build();
    }

    private double calculateWeightedAverage(double theoryScore, double practicalScore) {
        double weightedAverage = (theoryScore * 0.3) + (practicalScore * 0.7);
        return Math.round(weightedAverage * 100.0) / 100.0;
    }

    // Method to convert weighted average to Grade enum
    private Grade convertToGrade(double weightedAverage) {
        if (weightedAverage >= 8.5) return Grade.A;
        if (weightedAverage >= 8.0) return Grade.B_PLUS;
        if (weightedAverage >= 7.0) return Grade.B;
        if (weightedAverage >= 6.5) return Grade.C_PLUS;
        if (weightedAverage >= 5.5) return Grade.C;
        if (weightedAverage >= 4.0) return Grade.D;
        return Grade.F;
    }

    /**
     * Calculates the Cumulative Grade Point Average (CGPA) for a given list of student scores. The
     * CGPA is calculated based on the weighted average of the scores, considering only the scores
     * with a weighted average greater than or equal to 4.0.
     *
     * @param studentScores A list of student scores.
     * @return The calculated CGPA as a double value. If there are no valid scores (weighted average
     * >= 4.0), the function returns 0.
     */
    private double calculateCGPA(List<StudentScore> studentScores) {
        // Filter out scores with weighted average less than 4
        double totalCredits =
                studentScores.stream()
                        .filter(
                                score ->
                                        calculateWeightedAverage(
                                                score.getScoreTheory().doubleValue(),
                                                score.getScorePractical().doubleValue())
                                        >= 4.0)
                        .mapToDouble(score -> score.getEnrollment().getCourse().getCredits())
                        .sum();

        double weightedSum =
                studentScores.stream()
                        .filter(
                                score ->
                                        calculateWeightedAverage(
                                                score.getScoreTheory().doubleValue(),
                                                score.getScorePractical().doubleValue())
                                        >= 4.0)
                        .mapToDouble(
                                score ->
                                        score.getEnrollment().getCourse().getCredits()
                                        * calculateGrade4(
                                                calculateWeightedAverage(
                                                        score.getScoreTheory().doubleValue(),
                                                        score.getScorePractical().doubleValue())))
                        .sum();

        return totalCredits == 0 ? 0 : weightedSum / totalCredits;
    }

    /**
     * Calculates the Grade 4 equivalent for a given weighted average score.
     *
     * @param weightedAverage The weighted average score to convert.
     * @return The Grade 4 equivalent as a double value.
     */
    private double calculateGrade4(double weightedAverage) {
        if (weightedAverage >= 8.5) return 4.0;
        if (weightedAverage >= 8.0) return 3.5;
        if (weightedAverage >= 7.0) return 3.0;
        if (weightedAverage >= 6.5) return 2.5;
        if (weightedAverage >= 5.5) return 2.0;
        if (weightedAverage >= 4.0) return 1.0;
        return 0.0;
    }
}
