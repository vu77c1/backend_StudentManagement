package org.example.final_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.Student;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_department")
public class Department extends BaseEntity {

    @Id
    @Column(name = "department_id", length = 10)
    private String departmentId;

    @Column(name = "department_name", length = 50)
    private String departmentName;

    @Column(name = "description", length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "department")
    private Set<Course> courses = new LinkedHashSet<>();
}
