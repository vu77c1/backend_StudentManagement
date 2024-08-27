package org.example.final_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.request.UpdateStudentProfileDTO;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.repository.SearchRepository;
import org.example.final_project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@RequestMapping("/students")
@RestController
@Validated
@Slf4j
@Tag(name = "Student Controller")
public class StudentController {
  @Autowired private StudentService studentService;
  @Autowired private SearchRepository searchRepository;

  @GetMapping("")
  //    @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseData<?>> getStudents() {
    return ResponseEntity.ok(studentService.getAllStudent());
  }

  @PostMapping("")
  public ResponseEntity<ResponseData<?>> saveStudent(
      @Valid @RequestBody CreateStudentDto studentDto) {
    return ResponseEntity.ok(studentService.saveStudent(studentDto));
  }

  @PutMapping("/{studentId}")
  public ResponseEntity<ResponseData<?>> updateStudent(
      @PathVariable String studentId, @Valid @RequestBody UpdateStudentDto studentDto) {
    return ResponseEntity.ok(studentService.updateStudent(studentId, studentDto));
  }

  @DeleteMapping("/{studentId}")
  public ResponseEntity<ResponseData<?>> deleteStudent(@PathVariable String studentId) {
    return ResponseEntity.ok(studentService.deleteStudent(studentId));
  }

  @GetMapping("/{studentId}")
  public ResponseEntity<ResponseData<?>> findStudentById(@PathVariable String studentId) {
    return ResponseEntity.ok(studentService.getStudentById(studentId));
  }

  @Operation(
      summary = "Advance search query by criteria",
      description =
          "Send a request via this API to get user list by pageNo, pageSize and sort by multiple column")
  @GetMapping("/getStudent")
  public ResponseData<?> advanceSearchWithCriteria(
      @RequestParam(defaultValue = "0", required = false) int pageNo,
      @RequestParam(defaultValue = "5", required = false) int pageSize,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String address,
      @RequestParam(defaultValue = "") String... search) {
    log.info("Request advance search query by criteria");
    return new ResponseData<>(
        HttpStatus.OK.value(),
        "users",
        studentService.advanceSearchWithCriteria(pageNo, pageSize, sortBy, address, search));
  }

  @GetMapping("/getStudentList")
  public ResponseEntity<?> getAllStudents(
      @RequestParam(defaultValue = "0", required = false) int pageNo,
      @RequestParam(defaultValue = "5", required = false) int pageSize,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String search) {
    return ResponseEntity.ok(searchRepository.searchStudent(pageNo, pageSize, search, sortBy));
  }

  @GetMapping("/viewProfile/{studentId}")
  //    @PreAuthorize("hasRole('USER')")
  public ResponseEntity<ResponseData<?>> viewStudentProfile(@PathVariable String studentId) {
    return ResponseEntity.ok(studentService.viewStudentProfile(studentId));
  }

  @PostMapping("/upload/{studentId}")
  public ResponseEntity<ResponseData<?>> viewStudentProfile(
      @PathVariable String studentId, @ModelAttribute("files") MultipartFile files)
      throws IOException {
    return ResponseEntity.ok(studentService.studentImageUpload(studentId, files));
  }

  @GetMapping("/images/{imageName}")
  public ResponseEntity<?> viewImage(@PathVariable String imageName) {
    try {
      java.nio.file.Path imagePath = Paths.get("uploads/" + imageName);
      UrlResource resource = new UrlResource(imagePath.toUri());

      if (resource.exists()) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }
  @PostMapping("/updateProfile/{studentId}")
  public ResponseEntity<ResponseData<?>> updateProfile(@PathVariable String studentId,
                                                      @Valid @RequestBody UpdateStudentProfileDTO studentDto) {
    return ResponseEntity.ok(studentService.updateProfile(studentId, studentDto));
  }

}
