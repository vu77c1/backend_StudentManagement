package org.example.final_project.service;

import org.example.final_project.dto.request.CreateUniversityDto;
import org.example.final_project.dto.request.UpdateUniversityDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;
import org.example.final_project.model.University;

import java.util.List;

public interface UniversityService {
    ResponseData<List<University>> getAllUniversity();

    ResponseData<?> deleteUniversity(String universityId);
    ResponseData<?> saveUniversity(CreateUniversityDto request);


    ResponseData<?> updateUniversity(String universityId, UpdateUniversityDto request);

    ResponseData<University> getStudentById(String universityId);

}
