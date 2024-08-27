package org.example.final_project.service;

import org.example.final_project.dto.request.CreateCourseDto;
import org.example.final_project.dto.request.UpdateCourseDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;

public interface CourseService {
    PageResponse<?> getAllCourses(int pageNo, int pageSize, String sortBy);

    ResponseData<?> deleteCourse(String courseId);

    ResponseData<?> saveCourse(CreateCourseDto courseDto);

    ResponseData<?> updateCourse(String courseId, UpdateCourseDto courseDto);

    ResponseData<?> getCourseById(String courseId);
    // other methods...
}
