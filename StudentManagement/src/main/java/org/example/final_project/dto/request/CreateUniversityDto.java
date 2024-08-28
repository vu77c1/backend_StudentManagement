package org.example.final_project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * CreateUniversityDto
 */
@Getter
@Setter
public class CreateUniversityDto {
    private static final long serialVersionUID = 1L;

    private String universityId;
    private String universityName;
    private LocalDate foundedYear;
    private String address;
    private String phone;
    private List<String> departmentIds; // Danh sách các departmentId để thêm vào bảng university_department
    private String vision;
    private String mission;
    private String introduction;

}
