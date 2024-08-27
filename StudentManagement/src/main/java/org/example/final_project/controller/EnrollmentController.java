package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateEnrollmentDto;
import org.example.final_project.dto.request.UpdateEnrollmentDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/enrollments")
@Validated
@Slf4j
@Tag(name = "Enrollment Controller")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("")
    @Operation(summary = "Get all enrollments", description = "Retrieve a list of all enrollments")
    public ResponseEntity<?> getAllEnrollments(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                               @RequestParam(defaultValue = "5", required = false) int pageSize,
                                               @RequestParam(required = false) String sortBy) {
        log.info("Request get all enrollments");
        return new ResponseEntity<>(enrollmentService.getAllEnrollments(pageNo, pageSize, sortBy), HttpStatus.OK);
    }

    @GetMapping("/{enrollmentId}")
    @Operation(summary = "Get enrollment by ID", description = "Retrieve an enrollment by its ID")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long enrollmentId) {
        log.info("Request get enrollment by ID: {}", enrollmentId);
        return new ResponseEntity<>(enrollmentService.getEnrollmentById(enrollmentId), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "Create a new enrollment", description = "Create a new enrollment with provided details")
    public ResponseEntity<?> createEnrollment(@Valid @RequestBody CreateEnrollmentDto enrollmentDto) {
        log.info("Request to create a new enrollment");
        return new ResponseEntity<>(enrollmentService.createEnrollment(enrollmentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{enrollmentId}")
    @Operation(summary = "Update an existing enrollment", description = "Update an enrollment with provided details")
    public ResponseEntity<?> updateEnrollment(@PathVariable Long enrollmentId,
                                              @Valid @RequestBody UpdateEnrollmentDto enrollmentDto) {
        log.info("Request to update enrollment with ID: {}", enrollmentId);
        return new ResponseEntity<>(enrollmentService.updateEnrollment(enrollmentId, enrollmentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{enrollmentId}")
    @Operation(summary = "Delete an enrollment", description = "Delete an enrollment by its ID")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long enrollmentId) {
        log.info("Request to delete enrollment with ID: {}", enrollmentId);
        enrollmentService.deleteEnrollment(enrollmentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getEnrollmentByTeacherId/{teacherId}")
    public ResponseEntity<?> getEnrollmentByTeacherId(@PathVariable String teacherId) {
        log.info("Request to get enrollment for give teacher  ID: {}", teacherId);
        return new ResponseEntity<>(
                enrollmentService.getEnrollmentByTeacherId(teacherId),
                HttpStatus.OK
        );
    }
}
