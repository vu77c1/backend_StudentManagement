package org.example.final_project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class EnrollmentResponse {
    private Long enrollmentId;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
}
