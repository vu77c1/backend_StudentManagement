package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateDepartmentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.service.DepartmentService;
import org.example.final_project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/departments")
@RestController
@Validated
@Slf4j
@Tag(name = "Department Controller")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    /**
     * get all the department
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<ResponseData<?>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartment());
    }
    /*
     * delete the department by id
     */

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<ResponseData<?>> deleteDepartment(@PathVariable String departmentId) {
        return ResponseEntity.ok(departmentService.deleteDepartment(departmentId));
    }

    /**
     * saves the department to the database
     *
     * @param request
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ResponseData<?>> saveDepartment(@Valid @RequestBody CreateDepartmentDto request) {
        return ResponseEntity.ok(departmentService.saveDepartment(request));
    }

    /**
     * get the department by criteria
     *
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param search
     * @return
     */

    @GetMapping("/getAllDepartments")
    public ResponseData<?> advanceSearchWithCriteria(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                     @RequestParam(defaultValue = "5", required = false) int pageSize,
                                                     @RequestParam(required = false) String sortBy,
                                                     @RequestParam(defaultValue = "") String... search) {
        log.info("Request advance search query by criteria");
        return new ResponseData<>(HttpStatus.OK.value(), "success", departmentService.advanceSearchWithCriteria(pageNo, pageSize, sortBy, search));
    }

    /**
     * update department by id
     *
     * @param departmentId
     * @param request
     * @return
     */

    @PutMapping("/{departmentId}")
    public ResponseEntity<ResponseData<?>> updateDepartment(@PathVariable String departmentId,
                                                            @Valid @RequestBody UpdateDepartmentDto request) {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, request));
    }

    /**
     * get the department by department id
     *
     * @param departmentId
     * @return
     */

    @GetMapping("/{departmentId}")
    public ResponseEntity<ResponseData<?>> getDepartmentById(@PathVariable String departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

}
