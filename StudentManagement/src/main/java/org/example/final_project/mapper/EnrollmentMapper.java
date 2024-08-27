package org.example.final_project.mapper;

import org.example.final_project.dto.request.CreateEnrollmentDto;
import org.example.final_project.dto.request.UpdateEnrollmentDto;
import org.example.final_project.model.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "student", ignore = true) // Ignore if setting the student separately
    @Mapping(target = "course", ignore = true)  // Ignore if setting the course separately
    Enrollment toEntity(CreateEnrollmentDto dto);

    @Mapping(target = "student", ignore = true) // Ignore if setting the student separately
    @Mapping(target = "course", ignore = true)  // Ignore if setting the course separately
    Enrollment toEntity(UpdateEnrollmentDto dto);


    @Mapping(target = "student", ignore = true) // Ignore if setting the student separately
    @Mapping(target = "course", ignore = true)  // Ignore if setting the course separately
    void updateEntityFromDto(UpdateEnrollmentDto dto, @MappingTarget Enrollment entity);
}
