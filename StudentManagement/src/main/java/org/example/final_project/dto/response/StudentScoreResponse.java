package org.example.final_project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.final_project.util.Grade;

import java.io.Serializable;
import java.util.List;

/**
 * create StudentScoreResponse dto
 */
@Getter
@Setter
@Builder
public class StudentScoreResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String studentId;
    private String fullName;
    private String department;
    private String academicYear;
    private int totalCredits;
    private List<StudentScoreDetail> studentScores;
    private double gpa; // GPA tích lũy

    @Getter
    @Setter
    @Builder
    public static class StudentScoreDetail {
        private String subject;
        private int credits;
        private double theoryScore;
        private double practicalScore;
        private double weightedAverage;
        private Grade letterGrade;
        private double grade4;
    }
}
