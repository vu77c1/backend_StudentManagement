package org.example.final_project.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.final_project.model.Department;

import java.io.Serializable;

/**
 * create CreateCourseDto
 */
@Getter
@Setter
public class CreateCourseDto implements Serializable {
    @NotBlank(message = "courseId must not be blank")
    private String courseId;

    @NotBlank(message = "courseName must not be blank")
    private String name;

    private Integer credits;

    private String departmentId;
}
