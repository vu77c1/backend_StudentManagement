package org.example.final_project.service;

import org.example.final_project.dto.request.CreateStudentDto;
import org.example.final_project.dto.request.UpdateStudentDto;
import org.example.final_project.dto.request.UpdateStudentProfileDTO;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.Student;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Interface for student management.
 */
public interface StudentService {

    /**
     * Saves a new student.
     *
     * @param request the student details.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> saveStudent(CreateStudentDto request);

    /**
     * Updates an existing student.
     *
     * @param studentId the ID of the student.
     * @param request   the updated details.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> updateStudent(String studentId, UpdateStudentDto request);

    /**
     * Deletes a student by ID.
     *
     * @param studentId the ID of the student.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> deleteStudent(String studentId);

    /**
     * Retrieves a list of all students.
     *
     * @return a {@link ResponseData} containing a list of {@link Student}.
     */
    ResponseData<List<Student>> getAllStudent();

    /**
     * Retrieves a student by ID.
     *
     * @param studentId the ID of the student.
     * @return a {@link ResponseData} containing the {@link Student}.
     */
    ResponseData<Student> getStudentById(String studentId);

    /**
     * Performs advanced search with criteria.
     *
     * @param pageNo   the page number.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @param address  the address filter.
     * @param search   additional search criteria.
     * @return a {@link PageResponse} with search results.
     */
    PageResponse<?> advanceSearchWithCriteria(
            int pageNo, int pageSize, String sortBy, String address, String... search);

    /**
     * Performs advanced search with specifications.
     *
     * @param pageable   pagination information.
     * @param user       filters for user.
     * @param department filters for department.
     * @return a {@link PageResponse} with search results.
     */
    PageResponse<?> advanceSearchWithSpecifications(
            Pageable pageable, String[] user, String[] department);

    /**
     * Views a student's profile.
     *
     * @param studentId the ID of the student.
     * @return a {@link ResponseData} with the student's profile.
     */
    ResponseData<?> viewStudentProfile(String studentId);

    /**
     * Uploads an image for a student.
     *
     * @param studentId the ID of the student.
     * @param file      the image file.
     * @return a {@link ResponseData} with the result.
     * @throws IOException if an I/O error occurs.
     */
    ResponseData<?> studentImageUpload(String studentId, MultipartFile file) throws IOException;

    /**
     * Updates a student's profile.
     *
     * @param studentId  the ID of the student.
     * @param studentDto the profile details.
     * @return a {@link ResponseData} with the result.
     */
    ResponseData<?> updateProfile(String studentId, UpdateStudentProfileDTO studentDto);
}
