package org.example.final_project.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CreateDepartmentDto implements Serializable {
    @NotBlank(message = "departmentId must not be blank")
    private String departmentId;
    @NotBlank(message = "departmentName must not be blank")
    private String departmentName;
    private String description;
}
