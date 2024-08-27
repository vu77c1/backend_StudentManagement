package org.example.final_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_university_department")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UniversityDepartment {

    @EmbeddedId
    private UniversityDepartmentId id;

    @ManyToOne
    @JoinColumn(name = "university_id", insertable = false, updatable = false)
    private University university;

    @ManyToOne
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Embeddable
    @Data
    public static class UniversityDepartmentId {

        @Column(name = "university_id")
        private String universityId;

        @Column(name = "department_id")
        private String departmentId;

        public UniversityDepartmentId() {}

        public UniversityDepartmentId(String universityId, String departmentId) {
            this.universityId = universityId;
            this.departmentId = departmentId;
        }
    }
}
