package org.example.final_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.final_project.model.auth.User;
import org.example.final_project.util.Gender;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_student")
public class Student extends BaseEntity {

    @Id
    @Column(name = "student_id", length = 10)
    private String studentId;

    @Column(name = "academic_year")
    private int academicYear;

    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private LocalDate dateOfBirth;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "full_name", length = 70)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "temporary_address")
    private String temporaryAddress;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<Enrollment> enrollments = new LinkedHashSet<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user ;
}
