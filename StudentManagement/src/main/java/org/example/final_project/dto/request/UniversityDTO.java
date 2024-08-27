package org.example.final_project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class UniversityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String universityId;
    private String universityName;
    private String foundedYear;
    private String address;
    private String phone;
    private String vision;
    private String mission;
    private String introduction;
    private List<String> departmentIds; // Danh sách các departmentId để thêm vào bảng university_department



}
