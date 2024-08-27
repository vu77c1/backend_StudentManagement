package org.example.final_project.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.final_project.dto.validator.GenderSubset;
import org.example.final_project.util.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

import static org.example.final_project.util.Gender.*;

@Getter
@Setter
public class CreateStudentDto implements Serializable {
    @NotBlank(message = "Student ID must not be blank")
    String studentId;

    @Max(value = 4, message = "Academic year must be less than 4")
    Integer academicYear;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date of birth must not be null")
    LocalDate dateOfBirth;

    @Email(message = "Invalid email format")
    String email;

    @NotNull(message = "Full name must not be null")
    String fullName;

    @GenderSubset(anyOf = {MALE, FEMALE, OTHER})
    Gender gender;

    String permanentAddress;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number format")
    String phone;

    String temporaryAddress;

    String universityId;

    String departmentId;
}
