package org.example.final_project.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.request.CreateDepartmentDto;
import org.example.final_project.dto.request.UpdateDepartmentDto;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.dto.response.ResponseData;
import org.example.final_project.exception.ResourceNotFoundException;
import org.example.final_project.mapper.DepartmentMapper;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;
import org.example.final_project.repository.DepartmentRepository;
import org.example.final_project.repository.criteria.SearchCriteria;
import org.example.final_project.repository.criteria.SearchQueryCriteriaConsumer;
import org.example.final_project.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.final_project.util.AppConst.SEARCH_OPERATOR;
import static org.example.final_project.util.AppConst.SORT_BY;

@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public ResponseData<List<Department>> getAllDepartment() {
        List<Department> departments = departmentRepository.findAll();
        return new ResponseData<>(HttpStatus.OK.value(), "All departments retrieved successfully", departments);
    }

    @Override
    public PageResponse<?> advanceSearchWithCriteria(int pageNo, int pageSize, String sortBy, String... search) {
        log.info("Search department with search={} and sortBy={}", search, sortBy);

        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (search.length > 0) {
            Pattern pattern = Pattern.compile(SEARCH_OPERATOR);
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile(SORT_BY);
            for (String s : search) {
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List<Department> departments = getDepartments(pageNo, pageSize, criteriaList, sortBy);

        Long totalElements = getTotalElements(criteriaList);

        Page<Department> page = new PageImpl<>(departments, PageRequest.of(pageNo, pageSize), totalElements);

        return PageResponse.builder()
                .page(pageNo)
                .size(pageSize)
                .total(page.getTotalPages())
                .totalPages(page.getTotalElements())
                .items(departments)
                .build();
    }

    @Override
    public ResponseData<?> updateDepartment(String DepartmentId, UpdateDepartmentDto request) {
        //check if department exists
        Optional<Department> department = departmentRepository.findById(DepartmentId);
        if (department.isEmpty()) {
            throw new ResourceNotFoundException("Department not found with id " + DepartmentId);
        }

        //update department
        department.get().setDepartmentName(request.getDepartmentName());
        department.get().setDescription(request.getDescription());
        departmentRepository.save(department.get());
        return new ResponseData<>(HttpStatus.OK.value(), "Department updated successfully");
    }

    /**
     * create new a department
     *
     * @param request
     * @return
     */
    @Override
    public ResponseData<?> saveDepartment(CreateDepartmentDto request) {
        //check if department already exists
        if (departmentRepository.existsById(request.getDepartmentId())) {
            throw new ResourceNotFoundException("Department " + request.getDepartmentId() + " already exists");
        }
        //mapper to entity
        Department department = departmentMapper.toEntity(request);

        //save department
        departmentRepository.save(department);
        return new ResponseData<>(HttpStatus.OK.value(), "Department created successfully", department);
    }

    /**
     * delete a department from the database by departmentId
     *
     * @param departmentId
     * @return
     */

    @Override
    public ResponseData<?> deleteDepartment(String departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResourceNotFoundException("Department " + departmentId + " not found");
        }
        departmentRepository.deleteById(departmentId);
        return new ResponseData<>(HttpStatus.OK.value(), "Department deleted successfully");
    }

    /**
     * get department by departmentId
     *
     * @param DepartmentId
     * @return
     */
    @Override
    public ResponseData<?> getDepartmentById(String DepartmentId) {
        Optional<Department> department = departmentRepository.findById(DepartmentId);
        if (department.isEmpty()) {
            throw new ResourceNotFoundException("Department not found with id " + DepartmentId);
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Department retrieved successfully", department.get());
    }

    private List<Department> getDepartments(int pageNo, int pageSize, List<SearchCriteria> criteriaList, String sortBy) {
        log.info("-------------- getDepartments --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Department> query = criteriaBuilder.createQuery(Department.class);
        Root<Department> userRoot = query.from(Department.class);

        Predicate studentPredicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(studentPredicate, criteriaBuilder, userRoot);

        criteriaList.forEach(searchConsumer);
        studentPredicate = searchConsumer.getPredicate();
        query.where(studentPredicate);

        // Xử lý sắp xếp
        if (StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                String fieldName = matcher.group(1);
                String direction = matcher.group(3);
                if (direction.equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(userRoot.get(fieldName)));
                } else {
                    query.orderBy(criteriaBuilder.desc(userRoot.get(fieldName)));
                }
            }
        }

        // Tính toán firstResult và maxResults
        int firstResult = pageNo * pageSize;

        return entityManager.createQuery(query)
                .setFirstResult(firstResult) // Đặt vị trí bắt đầu
                .setMaxResults(pageSize) // Đặt số bản ghi trả về
                .getResultList();
    }

    private Long getTotalElements(List<SearchCriteria> params) {
        log.info("-------------- getTotalElements --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Department> root = query.from(Department.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, criteriaBuilder, root);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }


}
