package org.example.final_project.service;

import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.UpdateDepartmentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.Department;

import java.util.List;

public interface DepartmentService {
    ResponseData<List<Department>> getAllDepartment();

    PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search);

    ResponseData<?> updateDepartment(String DepartmentId, UpdateDepartmentDto request);

    ResponseData<?> saveDepartment(CreateDepartmentDto request);

    ResponseData<?> deleteDepartment(String departmentId);

    ResponseData<?> getDepartmentById(String DepartmentId);

}
