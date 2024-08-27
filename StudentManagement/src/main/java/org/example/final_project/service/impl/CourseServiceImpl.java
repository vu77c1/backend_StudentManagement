package org.example.final_project.service.impl;

import org.example.final_project.dto.request.CreateCourseDto;
import org.example.final_project.dto.request.UpdateCourseDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.mapper.CourseMapper;
import org.example.final_project.model.Course;
import org.example.final_project.model.Department;
import org.example.final_project.repository.CourseRepository;
import org.example.final_project.repository.DepartmentRepository;
import org.example.final_project.service.CourseService;
import org.example.final_project.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.final_project.util.AppConst.SORT_BY;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    CourseMapper courseMapper;

    /**
     * get all courses
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @return
     */
    @Override
    public PageResponse<?> getAllCourses(int pageNo, int pageSize, String sortBy) {
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
        Page<Course> coursePage = courseRepository.findAll(pageable);

        return PageResponse.builder()
                .page(coursePage.getNumber() + 1)
                .size(coursePage.getSize())
                .total(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .items(coursePage.getContent())
                .build();

    }

    /**
     * delete a course by id
     *
     * @param courseId
     * @return
     */

    @Override
    public ResponseData<?> deleteCourse(String courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }
        courseRepository.deleteById(courseId);
        return new ResponseData<>(HttpStatus.OK.value(), "Course deleted successfully");
    }

    /**
     * insert a course to database
     *
     * @param courseDto
     * @return
     */

    @Override
    public ResponseData<?> saveCourse(CreateCourseDto courseDto) {
        //check courseId already exists
        if (courseRepository.existsById(courseDto.getCourseId())) {
            throw new ResourceNotFoundException("Course already exists with id " + courseDto.getCourseId());
        }
        //check department exists
        Optional<Department> department = departmentRepository.findById(courseDto.getDepartmentId());
        if (department.isEmpty()) {
            throw new ResourceNotFoundException("Department not found with id " + courseDto.getDepartmentId());
        }
        //save course

        Course course = courseMapper.toEntity(courseDto);
        course.setDepartment(department.get());
        courseRepository.save(course);
        return new ResponseData<>(HttpStatus.OK.value(), "Course saved successfully", course);
    }

    @Override
    public ResponseData<?> updateCourse(String courseId, UpdateCourseDto courseDto) {
        // Find the existing course
        Optional<Course> existingCourseOptional = courseRepository.findById(courseId);
        if (existingCourseOptional.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }

        Course existingCourse = existingCourseOptional.get();

        // Find the department
        Optional<Department> departmentOptional = departmentRepository.findById(courseDto.getDepartmentId());
        if (departmentOptional.isEmpty()) {
            throw new ResourceNotFoundException("Department not found with id " + courseDto.getDepartmentId());
        }

        // Update the existing course with values from the DTO
        courseMapper.updateEntityFromDto(courseDto, existingCourse);

        // Set the department
        existingCourse.setDepartment(departmentOptional.get());

        // Save the updated course
        courseRepository.save(existingCourse);

        return new ResponseData<>(HttpStatus.OK.value(), "Course updated successfully");
    }

    /**
     * get course by id
     *
     * @param courseId
     * @return
     */

    @Override
    public ResponseData<?> getCourseById(String courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            throw new ResourceNotFoundException("Course not found with id " + courseId);
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Course found successfully", course.get());
    }

}
