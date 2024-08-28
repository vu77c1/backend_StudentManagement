package org.example.final_project.service;

import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.UpdateDepartmentDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.model.Department;

import java.util.List;

/**
 * Interface for managing departments.
 */
public interface DepartmentService {

    /**
     * Retrieves a list of all departments.
     *
     * @return a {@link ResponseData} containing a list of all departments.
     */
    ResponseData<List<Department>> getAllDepartment();

    /**
     * Performs advanced search on departments with pagination and sorting.
     *
     * @param pageNo the page number.
     * @param pageSize the number of items per page.
     * @param sortBy the field to sort by.
     * @param search search criteria.
     * @return a {@link PageResponse} containing the search results.
     */
    PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search);

    /**
     * Updates an existing department.
     *
     * @param departmentId the ID of the department to update.
     * @param request the updated department details.
     * @return a {@link ResponseData} with the result of the update.
     */
    ResponseData<?> updateDepartment(String departmentId, UpdateDepartmentDto request);

    /**
     * Saves a new department.
     *
     * @param request the details of the department to create.
     * @return a {@link ResponseData} with the result of the creation.
     */
    ResponseData<?> saveDepartment(CreateDepartmentDto request);

    /**
     * Deletes a department by its ID.
     *
     * @param departmentId the ID of the department to delete.
     * @return a {@link ResponseData} with the result of the deletion.
     */
    ResponseData<?> deleteDepartment(String departmentId);

    /**
     * Retrieves a department by its ID.
     *
     * @param departmentId the ID of the department to retrieve.
     * @return a {@link ResponseData} containing the department details.
     */
    ResponseData<?> getDepartmentById(String departmentId);
}
