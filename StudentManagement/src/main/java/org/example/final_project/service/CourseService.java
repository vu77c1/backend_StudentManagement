package org.example.final_project.service;

import org.example.final_project.dto.request.CreateCourseDto;
import org.example.final_project.dto.request.UpdateCourseDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;

/**
 * Interface for managing courses.
 */
public interface CourseService {

    /**
     * Retrieves a paginated list of all courses with sorting.
     *
     * @param pageNo   the page number.
     * @param pageSize the number of items per page.
     * @param sortBy   the field to sort by.
     * @return a {@link PageResponse} containing the list of courses.
     */
    PageResponse<?> getAllCourses(int pageNo, int pageSize, String sortBy);

    /**
     * Deletes a course by its ID.
     *
     * @param courseId the ID of the course to delete.
     * @return a {@link ResponseData} with the result of the deletion.
     */
    ResponseData<?> deleteCourse(String courseId);

    /**
     * Saves a new course.
     *
     * @param courseDto the details of the course to create.
     * @return a {@link ResponseData} with the result of the creation.
     */
    ResponseData<?> saveCourse(CreateCourseDto courseDto);

    /**
     * Updates an existing course.
     *
     * @param courseId  the ID of the course to update.
     * @param courseDto the updated course details.
     * @return a {@link ResponseData} with the result of the update.
     */
    ResponseData<?> updateCourse(String courseId, UpdateCourseDto courseDto);

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId the ID of the course to retrieve.
     * @return a {@link ResponseData} containing the course details.
     */
    ResponseData<?> getCourseById(String courseId);
    // other methods...
}
