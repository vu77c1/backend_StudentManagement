package org.example.final_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_university")
public class University extends BaseEntity {

    @Id
    @Column(name = "university_id", length = 10)
    private String universityId;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "founded_year")
    @Temporal(TemporalType.DATE)
    private LocalDate foundedYear;

    @Column(name = "introduction", length = 1000)
    private String introduction;

    @Column(name = "mission", length = 1000)
    private String mission;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "university_name", length = 100)
    private String universityName;

    @Column(name = "vision", length = 1000)
    private String vision;

//    @ManyToMany
//    @JoinTable(
//            name = "university_department",
//            joinColumns = @JoinColumn(name = "university_id"),
//            inverseJoinColumns = @JoinColumn(name = "department_id")
//    )
//    private List<Department> departments;
    @JsonIgnore
    @OneToMany(mappedBy = "university")
    private List<Student> students;

}
