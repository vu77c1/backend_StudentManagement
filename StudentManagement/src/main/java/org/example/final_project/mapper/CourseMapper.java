package org.example.final_project.mapper;

import org.example.final_project.dto.request.CreateCourseDto;
import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.UpdateCourseDto;
import org.example.final_project.model.Course;
import org.example.final_project.model.Department;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CreateCourseDto createCourseDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateCourseDto courseDto, @MappingTarget Course existingCourse);
}
