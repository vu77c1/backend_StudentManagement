package org.example.final_project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.request.UpdateStudentProfileDTO;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.dto.response.StudentProfileResponse;
import org.example.final_project.exception.InvalidDataException;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.mapper.StudentMapper;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;
import org.example.final_project.model.University;
import org.example.final_project.model.auth.User;
import org.example.final_project.repository.*;
import org.example.final_project.service.StudentService;
import org.example.final_project.util.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired SearchRepository searchRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired DepartmentRepository departmentRepository;
    @Autowired UniversityRepository universityRepository;
    @Autowired StudentMapper studentMapper;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;

    /**
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> saveStudent(CreateStudentDto request) {
        // Create a logger instance
        Logger log = LoggerFactory.getLogger(StudentService.class);

        log.info("Request to add student with ID: {}", request.getStudentId());

        // Check if a Student with the given studentId already exists
        if (studentRepository.existsById(request.getStudentId())) {
            log.warn("Student ID already exists: {}", request.getStudentId());
            throw new InvalidDataException("Student ID already exists: " + request.getStudentId());
        }

        // Check if the Department with the given departmentId exists
        Department department =
                departmentRepository
                        .findById(request.getDepartmentId())
                        .orElseThrow(
                                () -> {
                                    log.error(
                                            "Department not found with id: {}",
                                            request.getDepartmentId());
                                    return new ResourceNotFoundException(
                                            "Department not found with id "
                                                    + request.getDepartmentId());
                                });

        // Check if the University with the given universityId exists
        University university =
                universityRepository
                        .findById(request.getUniversityId())
                        .orElseThrow(
                                () -> {
                                    log.error(
                                            "University not found with id: {}",
                                            request.getUniversityId());
                                    return new ResourceNotFoundException(
                                            "University not found with id "
                                                    + request.getUniversityId());
                                });

        // Convert StudentDto to Student entity
        Student student = studentMapper.toEntity(request);
        // Set the Department and University for the Student entity
        student.setDepartment(department);
        student.setUniversity(university);

        // save user
        if (userRepository.existsByUsername(request.getStudentId())) {
            log.error("User already exists with username: {}", request.getStudentId());
            throw new InvalidDataException(
                    "User already exists with username: " + request.getStudentId());
        }
        User user = new User();
        user.setUsername(request.getStudentId());
        String password = passwordEncoder.encode(request.getStudentId());
        user.setPassword(password);
        user.setEmail(request.getEmail());
        user.setGender(request.getGender());
        user.setPhone(request.getPhone());
        user.setDateOfBirth(localDateToDate(request.getDateOfBirth()));
        user.setStatus(UserStatus.ACTIVE);
        student.setUser(user);
        userRepository.save(user);

        // Save the Student entity to the database
        studentRepository.save(student);

        log.info("Student added successfully with ID: {}", student.getStudentId());

        // Return a success response
        return new ResponseData<>(
                HttpStatus.OK.value(), "Student has been added successfully", null);
    }

    private Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * @param studentId
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> updateStudent(String studentId, UpdateStudentDto request) {
        // Create a logger instance
        Logger log = LoggerFactory.getLogger(StudentService.class);

        log.info("Request to update student with ID: {}", studentId);

        // Check if the Student with the given studentId exists
        Student existingStudent =
                studentRepository
                        .findById(studentId)
                        .orElseThrow(
                                () -> {
                                    log.error("Student not found with id: {}", studentId);
                                    return new ResourceNotFoundException(
                                            "Student not found with id " + studentId);
                                });

        // Check if the Department with the given departmentId exists
        Department department =
                departmentRepository
                        .findById(request.getDepartmentId())
                        .orElseThrow(
                                () -> {
                                    log.error(
                                            "Department not found with id: {}",
                                            request.getDepartmentId());
                                    return new ResourceNotFoundException(
                                            "Department not found with id "
                                                    + request.getDepartmentId());
                                });

        // Check if the University with the given universityId exists
        University university =
                universityRepository
                        .findById(request.getUniversityId())
                        .orElseThrow(
                                () -> {
                                    log.error(
                                            "University not found with id: {}",
                                            request.getUniversityId());
                                    return new ResourceNotFoundException(
                                            "University not found with id "
                                                    + request.getUniversityId());
                                });

        // Map the fields from the StudentDto to the existing Student entity
        studentMapper.partialUpdate(request, existingStudent);

        // Set the updated Department and University
        existingStudent.setDepartment(department);
        existingStudent.setUniversity(university);

        // Save the updated Student entity to the database
        studentRepository.save(existingStudent);

        log.info("Student updated successfully with ID: {}", existingStudent.getStudentId());

        // Return a success response
        return new ResponseData<>(
                HttpStatus.OK.value(), "Student has been updated successfully", null);
    }

    /**
     * delete a student
     *
     * @param studentId
     * @return
     */
    @Override
    public ResponseData<?> deleteStudent(String studentId) {
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
        studentRepository.deleteById(studentId);
        return new ResponseData<>(HttpStatus.OK.value(), "Student has been deleted successfully");
    }

    /**
     * Get all students
     *
     * @return list of students
     */
    @Override
    public ResponseData<List<Student>> getAllStudent() {
        // Get all students from the database
        List<Student> students = studentRepository.findAll();
        // Return a success response with the list of students
        return new ResponseData<>(
                HttpStatus.OK.value(), "All students retrieved successfully", students);
    }

    @Override
    public ResponseData<Student> getStudentById(String studentId) {
        Student student =
                studentRepository
                        .findById(studentId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Student not found with id " + studentId));
        return new ResponseData<>(HttpStatus.OK.value(), "Student retrieved successfully", student);
    }

    @Override
    public PageResponse<?> advanceSearchWithCriteria(
            int pageNo, int pageSize, String sortBy, String address, String... search) {
        return searchRepository.searchStudentByCriteria(pageNo, pageSize, sortBy, address, search);
    }

    @Override
    public PageResponse<?> advanceSearchWithSpecifications(
            Pageable pageable, String[] user, String[] department) {
        return null;
    }

    @Override
    public ResponseData<?> viewStudentProfile(String studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with id " + studentId);
        }
        StudentProfileResponse res =
                StudentProfileResponse.builder()
                        .fullName(student.get().getFullName())
                        .studentId(student.get().getStudentId())
                        .department(student.get().getDepartment().getDepartmentName())
                        .email(student.get().getEmail())
                        .phone(student.get().getPhone())
                        .profilePicture(student.get().getUser().getImageUrl())
                        .address(student.get().getTemporaryAddress())
                        .build();
        return new ResponseData<>(
                HttpStatus.OK.value(), "Student profile retrieved successfully", res);
    }

    @Override
    public ResponseData<?> studentImageUpload(String studentId, MultipartFile file)
            throws IOException {
        Optional<User> user = userRepository.findByUsername(studentId);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("user not found with id " + studentId);
        }
        String uniqueFilename = storeFile(file);
        user.get().setImageUrl(uniqueFilename);
        userRepository.save(user.get());
        return new ResponseData<>(
                HttpStatus.OK.value(), "user image uploaded successfully", uniqueFilename);
    }

    @Override
    public ResponseData<?> updateProfile(String studentId, UpdateStudentProfileDTO studentDto) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("user not found with id " + studentId);
        }
        Student optionalStudent = student.get();
        optionalStudent.setEmail(studentDto.getEmail());
        optionalStudent.setPhone(studentDto.getPhoneNumber());
        optionalStudent.setTemporaryAddress(studentDto.getAddress());

        studentRepository.save(optionalStudent);
        return new ResponseData<>(HttpStatus.OK.value(), "student profile updated successfully");
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}
