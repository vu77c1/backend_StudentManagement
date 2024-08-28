package org.example.final_project.service;

import org.example.final_project.dto.request.CreateUniversityDto;
import org.example.final_project.dto.request.UpdateUniversityDto;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.University;

import java.util.List;

/**
 * Interface for university management.
 */
public interface UniversityService {

    /**
     * Gets all universities.
     *
     * @return a {@link ResponseData} containing a list of {@link University}.
     */
    ResponseData<List<University>> getAllUniversity();

    /**
     * Deletes a university by its ID.
     *
     * @param universityId the ID of the university.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> deleteUniversity(String universityId);

    /**
     * Saves a new university.
     *
     * @param request the university details.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> saveUniversity(CreateUniversityDto request);

    /**
     * Updates an existing university.
     *
     * @param universityId the ID of the university.
     * @param request      the updated details.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> updateUniversity(String universityId, UpdateUniversityDto request);

    /**
     * Gets a university by its ID.
     *
     * @param universityId the ID of the university.
     * @return a {@link ResponseData} containing the {@link University}.
     */
    ResponseData<University> getStudentById(String universityId);
}
