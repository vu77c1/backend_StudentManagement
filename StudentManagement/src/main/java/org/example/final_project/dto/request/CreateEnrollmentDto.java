package org.example.final_project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateEnrollmentDto implements Serializable {
    @NotNull(message = "Student ID cannot be null")
    private String studentId;

    @NotNull(message = "Course ID cannot be null")
    private String courseId;

}
