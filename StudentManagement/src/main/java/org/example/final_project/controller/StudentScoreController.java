package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.StudentScoreRequest;
import org.example.final_project.service.StudentScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/StudentScore")
@Validated
@Slf4j
@Tag(name = "StudentScore Controller")
@RequiredArgsConstructor
public class StudentScoreController {
    @Autowired
    private final StudentScoreService service;

    @GetMapping("/{studentId}")
    public ResponseEntity<?> getAllCourses(@PathVariable String studentId) {
        log.info("Request get all scores of student by ID: {}", studentId);
        return new ResponseEntity<>(service.viewStudentScore(studentId), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<?> addScore(@RequestBody StudentScoreRequest request) {
        return new ResponseEntity<>(service.insertStudentScore(request), HttpStatus.OK);
    }

}
