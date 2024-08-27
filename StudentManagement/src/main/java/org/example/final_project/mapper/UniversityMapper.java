package org.example.final_project.mapper;

import org.example.final_project.dto.request.CreateUniversityDto;
import org.example.final_project.dto.request.UpdateUniversityDto;
import org.example.final_project.model.University;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UniversityMapper {

    // Map CreateUniversityDto to University entity
    University toEntity(CreateUniversityDto createUniversityDto);

    // Map UpdateUniversityDto to University entity
    University toEntity(UpdateUniversityDto updateUniversityDto);

    // Partial update for University entity from CreateUniversityDto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(CreateUniversityDto createUniversityDto, @MappingTarget University university);

    // Partial update for University entity from UpdateUniversityDto
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(UpdateUniversityDto updateUniversityDto, @MappingTarget University university);
}
