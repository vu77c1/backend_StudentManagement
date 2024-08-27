package org.example.final_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDepartmentDto {
    @NotBlank(message = "departmentName must not be blank")
    private String departmentName;
    private String description;
}
