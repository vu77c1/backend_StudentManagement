package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateCourseDto;
import org.example.final_project.dto.request.UpdateCourseDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@Validated
@Slf4j
@Tag(name = "Course Controller")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("")
    public ResponseEntity<?> getAllCourses(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                           @RequestParam(defaultValue = "5", required = false) int pageSize,
                                           @RequestParam(required = false) String sortBy) {
        log.info("Request get all of course");
        return new ResponseEntity<>(courseService.getAllCourses(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) {
        log.info("delete course with ID: {}", courseId);
        return new ResponseEntity<>(courseService.deleteCourse(courseId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> saveCourse(@Valid @RequestBody CreateCourseDto courseDto) {
        log.info("save course");
        return new ResponseEntity<>(courseService.saveCourse(courseDto), HttpStatus.OK);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable String courseId,
                                          @Valid @RequestBody UpdateCourseDto courseDto) {
        log.info("update course  by ID: {}", courseId);
        return new ResponseEntity<>(courseService.updateCourse(courseId,courseDto), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?>getCourseById(@PathVariable String courseId) {
        log.info("get course by ID: {}", courseId);
        return new ResponseEntity<>(courseService.getCourseById(courseId), HttpStatus.OK);
    }

}
