package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateUniversityDto;
import org.example.final_project.dto.request.UpdateUniversityDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling operations related to universities.
 */
@RequestMapping("/universities")
@RestController
@Validated
@Slf4j
@Tag(name = "University Controller")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    /**
     * Retrieves all universities.
     *
     * @return a {@link ResponseEntity} containing a {@link ResponseData} with the list of all universities.
     */
    @GetMapping
    public ResponseEntity<ResponseData<?>> getAllUniversities() {
        log.info("Fetching all universities");
        ResponseData<?> response = universityService.getAllUniversity();
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a university by its ID.
     *
     * @param universityId the ID of the university to be deleted.
     * @return a {@link ResponseEntity} containing a {@link ResponseData} with the result of the deletion operation.
     */
    @DeleteMapping("/{universityId}")
    public ResponseEntity<ResponseData<?>> deleteUniversity(@PathVariable String universityId) {
        log.info("Request to delete university with ID: {}", universityId);
        ResponseData<?> response = universityService.deleteUniversity(universityId);
        return ResponseEntity.ok(response); // Consider using 204 No Content for delete operations
    }

    /**
     * Creates a new university.
     *
     * @param request the {@link CreateUniversityDto} containing the details of the university to be created.
     * @return a {@link ResponseEntity} containing a {@link ResponseData} with the result of the creation operation.
     */
    @PostMapping
    public ResponseEntity<ResponseData<?>> saveUniversity(@Valid @RequestBody CreateUniversityDto request) {
        log.info("Request to create university with ID: {}", request.getUniversityId());
        ResponseData<?> response = universityService.saveUniversity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created status
    }

    /**
     * Updates an existing university with the provided information.
     *
     * @param universityId the ID of the university to be updated.
     * @param request      the {@link UpdateUniversityDto} object containing the updated details.
     * @return a {@link ResponseEntity} containing the result of the update operation wrapped in a {@link ResponseData}.
     * @throws ResourceNotFoundException if the university with the specified ID does not exist.
     */
    @PutMapping("/{universityId}")
    public ResponseEntity<ResponseData<?>> updateUniversity(
            @PathVariable String universityId,
            @Valid @RequestBody UpdateUniversityDto request) {
        return ResponseEntity.ok(universityService.updateUniversity(universityId, request));
    }
    @GetMapping("/{universityId}")
    public ResponseEntity<ResponseData<?>> findUniversityById(@PathVariable String universityId) {
        return ResponseEntity.ok(universityService.getStudentById(universityId));
    }

}
