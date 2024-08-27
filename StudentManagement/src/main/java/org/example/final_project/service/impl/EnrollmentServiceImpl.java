package org.example.final_project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateEnrollmentDto;
import org.example.final_project.dto.request.UpdateEnrollmentDto;
import org.example.final_project.dto.response.EnrollmentResponse;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.mapper.EnrollmentMapper;
import org.example.final_project.model.Enrollment;
import org.example.final_project.model.Course;
import org.example.final_project.model.Student;
import org.example.final_project.model.StudentScore;
import org.example.final_project.repository.EnrollmentRepository;
import org.example.final_project.repository.CourseRepository;
import org.example.final_project.repository.StudentRepository;
import org.example.final_project.service.EnrollmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.example.final_project.util.AppConst.SORT_BY;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public PageResponse<?> getAllEnrollments(int pageNo, int pageSize, String sortBy) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1;
        }
        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts));
        Page<Enrollment> coursePage = enrollmentRepository.findAll(pageable);

        return PageResponse.builder()
                .page(coursePage.getNumber() + 1)
                .size(coursePage.getSize())
                .total(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .items(coursePage.getContent())
                .build();
    }

    @Override
    public ResponseData<?> getEnrollmentById(Long enrollmentId) {
        log.info("Fetching enrollment with ID: {}", enrollmentId);
        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> new ResponseData<>(HttpStatus.OK.value(), "Enrollment found successfully", enrollment))
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id " + enrollmentId));
    }

    @Override
    public ResponseData<?> createEnrollment(CreateEnrollmentDto enrollmentDto) {
        log.info("Creating enrollment: {}", enrollmentDto);

        Optional<Student> student = studentRepository.findById(enrollmentDto.getStudentId());
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with id " + enrollmentDto.getStudentId());
        }

        Optional<Course> course = courseRepository.findById(enrollmentDto.getCourseId());
        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id " + enrollmentDto.getCourseId());
        }

        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDto);
        enrollment.setStudent(student.get());
        enrollment.setCourse(course.get());
        enrollment.setEnrollmentDate(LocalDate.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return new ResponseData<>(HttpStatus.OK.value(), "Enrollment created successfully", savedEnrollment);
    }

    @Override
    public ResponseData<?> updateEnrollment(Long enrollmentId, UpdateEnrollmentDto enrollmentDto) {
        log.info("Updating enrollment with ID: {}", enrollmentId);

        Enrollment existingEnrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id " + enrollmentId));

        Optional<Student> student = studentRepository.findById(enrollmentDto.getStudentId());
        if (student.isEmpty()) {
            throw new ResourceNotFoundException("Student not found with id " + enrollmentDto.getStudentId());
        }

        Optional<Course> course = courseRepository.findById(enrollmentDto.getCourseId());
        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id " + enrollmentDto.getCourseId());
        }

        enrollmentMapper.updateEntityFromDto(enrollmentDto, existingEnrollment);
        existingEnrollment.setStudent(student.get());
        existingEnrollment.setCourse(course.get());

        Enrollment updatedEnrollment = enrollmentRepository.save(existingEnrollment);

        return new ResponseData<>(HttpStatus.OK.value(), "Enrollment updated successfully", updatedEnrollment);
    }

    @Override
    public void deleteEnrollment(Long enrollmentId) {
        log.info("Deleting enrollment with ID: {}", enrollmentId);

        if (!enrollmentRepository.existsById(enrollmentId)) {
            throw new ResourceNotFoundException("Enrollment not found with id " + enrollmentId);
        }
        enrollmentRepository.deleteById(enrollmentId);
    }

    @Override
    public ResponseData<?> getEnrollmentByTeacherId(String teacherId) {
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsWithoutScores(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        List<EnrollmentResponse> responses = enrollments.stream()
                .map(enrollment -> EnrollmentResponse.builder()
                        .enrollmentId(enrollment.getEnrollmentId())
                        .courseId(enrollment.getCourse().getCourseId())
                        .courseName(enrollment.getCourse().getName())
                        .studentId(enrollment.getStudent().getStudentId())
                        .studentName(enrollment.getStudent().getFullName()) // Sử dụng tên đầy đủ nếu có
                        .build())
                .collect(Collectors.toList());

        return new ResponseData<>(HttpStatus.OK.value(), "success", responses);
    }

}