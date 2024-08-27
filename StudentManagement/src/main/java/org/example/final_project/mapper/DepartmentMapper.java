package org.example.final_project.mapper;

import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DepartmentMapper {
    Department toEntity(CreateDepartmentDto createDepartmentDto);

}

