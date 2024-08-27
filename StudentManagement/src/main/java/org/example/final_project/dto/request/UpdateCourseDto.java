package org.example.final_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class UpdateCourseDto implements Serializable {


    @NotBlank(message = "courseName must not be blank")
    private String name;

    private Integer credits;

    private String departmentId;
}
