package org.example.final_project.service;

import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.request.UpdateStudentProfileDTO;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StudentService {

    ResponseData<?> saveStudent(CreateStudentDto request);

    ResponseData<?> updateStudent(String studentId, UpdateStudentDto request);

    ResponseData<?> deleteStudent(String studentId);

    ResponseData<List<Student>> getAllStudent();

    ResponseData<Student> getStudentById(String studentId);

    PageResponse<?> advanceSearchWithCriteria(
            int pageNo, int pageSize, String sortBy, String address, String... search);

    PageResponse<?> advanceSearchWithSpecifications(
            Pageable pageable, String[] user, String[] department);

    ResponseData<?> viewStudentProfile(String studentId);

    ResponseData<?> studentImageUpload(String studentId, MultipartFile file) throws IOException;

    ResponseData<?> updateProfile(String studentId, UpdateStudentProfileDTO studentDto);
}
