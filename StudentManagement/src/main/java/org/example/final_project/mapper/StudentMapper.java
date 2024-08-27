package org.example.final_project.mapper;

import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.model.Student;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface StudentMapper {
    Student toEntity(CreateStudentDto createStudentDto);

    Student toEntity(UpdateStudentDto updateStudentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Student partialUpdate(CreateStudentDto createStudentDto, @MappingTarget Student student);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UpdateStudentDto updateStudentDto, @MappingTarget Student student);
}