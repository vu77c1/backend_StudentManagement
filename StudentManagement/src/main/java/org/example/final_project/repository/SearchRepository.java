package org.example.final_project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.example.final_project.dto.response.PageResponse;
import org.example.final_project.model.Department;
import org.example.final_project.model.Student;
import org.example.final_project.repository.criteria.SearchCriteria;
import org.example.final_project.repository.criteria.SearchQueryCriteriaConsumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.example.final_project.util.AppConst.SEARCH_OPERATOR;
import static org.example.final_project.util.AppConst.SORT_BY;

@Component
@Slf4j
public class SearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final String LIKE_FORMAT = "%%%s%%";


    /**
     * Advance search student by criterias
     *
     * @param offset
     * @param pageSize
     * @param sortBy
     * @param search
     * @return
     */
    public PageResponse<?> searchStudentByCriteria(int offset, int pageSize, String sortBy, String address, String... search) {
        log.info("Search student with search={} and sortBy={}", search, sortBy);

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

        List<Student> students = getStudents(offset, pageSize, criteriaList, address, sortBy);

        Long totalElements = getTotalElements(criteriaList);

        Page<Student> page = new PageImpl<>(students, PageRequest.of(offset, pageSize), totalElements);

        return PageResponse.builder()
                .page(offset)
                .size(pageSize)
                .total(page.getTotalPages())
                .items(students)
                .build();
    }

    /**
     * Get all Student with conditions
     *
     * @param offset
     * @param pageSize
     * @param criteriaList
     * @param sortBy
     * @return
     */
    private List<Student> getStudents(int offset, int pageSize, List<SearchCriteria> criteriaList, String departmentName, String sortBy) {
        log.info("-------------- getStudents --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> query = criteriaBuilder.createQuery(Student.class);
        Root<Student> userRoot = query.from(Student.class);

        Predicate studentPredicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(studentPredicate, criteriaBuilder, userRoot);

        if (StringUtils.hasLength(departmentName)) {
            Join<Department, Student> departmentStudentJoin = userRoot.join("department");
            Predicate departmentPredicate = criteriaBuilder.equal(departmentStudentJoin.get("departmentId"), departmentName);
            query.where(studentPredicate, departmentPredicate);
        } else {
        criteriaList.forEach(searchConsumer);
        studentPredicate = searchConsumer.getPredicate();
        query.where(studentPredicate);
        }
        Pattern pattern = Pattern.compile(SORT_BY);
        if (StringUtils.hasLength(sortBy)) {
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

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /**
     * Count students with conditions
     *
     * @param params
     * @return
     */
    private Long getTotalElements(List<SearchCriteria> params) {
        log.info("-------------- getTotalElements --------------");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Student> root = query.from(Student.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchQueryCriteriaConsumer searchConsumer = new SearchQueryCriteriaConsumer(predicate, criteriaBuilder, root);
        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }
    public PageResponse<?> searchStudent(int pageNo, int pageSize, String search, String sortBy) {
        // Log thông tin khi bắt đầu thực hiện tìm kiếm
        log.info("Execute search user with keyword={}", search);

        // Khởi tạo câu truy vấn SQL cơ bản
        StringBuilder sqlQuery = new StringBuilder("SELECT u FROM Student u WHERE 1=1");

        // Nếu có từ khóa tìm kiếm, thêm điều kiện tìm kiếm vào câu truy vấn
        if (StringUtils.hasLength(search)) {
            // Thêm điều kiện tìm kiếm theo studentId và fullName, đảm bảo dùng dấu ngoặc để xử lý đúng với OR
            sqlQuery.append(" AND (lower(u.studentId) like lower(:studentId)");
            sqlQuery.append(" OR lower(u.fullName) like lower(:fullName))");
        }

        // Nếu có yêu cầu sắp xếp, thêm điều kiện sắp xếp vào câu truy vấn
        if (StringUtils.hasLength(sortBy)) {
            // Sử dụng Pattern để kiểm tra và trích xuất thông tin sắp xếp hợp lệ
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                // Thêm điều kiện ORDER BY vào câu truy vấn với trường và thứ tự sắp xếp hợp lệ
                sqlQuery.append(String.format(" ORDER BY u.%s %s", matcher.group(1), matcher.group(3)));
            }
        }

        // Tạo và thực thi câu truy vấn để lấy danh sách sinh viên
        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        if (StringUtils.hasLength(search)) {
            // Đặt tham số tìm kiếm cho câu truy vấn
            selectQuery.setParameter("studentId", String.format(LIKE_FORMAT, search));
            selectQuery.setParameter("fullName", String.format(LIKE_FORMAT, search));
        }
        // Thiết lập kết quả bắt đầu từ vị trí đầu tiên (offset)
        selectQuery.setFirstResult(pageNo * pageSize);
        // Thiết lập số lượng kết quả tối đa
        selectQuery.setMaxResults(pageSize);
        // Lấy danh sách kết quả
        List<?> users = selectQuery.getResultList();

        // Khởi tạo câu truy vấn đếm số lượng sinh viên
        StringBuilder sqlCountQuery = new StringBuilder("SELECT COUNT(*) FROM Student u WHERE 1=1");
        if (StringUtils.hasLength(search)) {
            // Thêm điều kiện tìm kiếm cho câu truy vấn đếm
            sqlCountQuery.append(" AND (lower(u.studentId) like lower(?1)");
            sqlCountQuery.append(" OR lower(u.fullName) like lower(?2))");
        }

        // Tạo và thực thi câu truy vấn đếm
        Query countQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            // Đặt tham số tìm kiếm cho câu truy vấn đếm
            countQuery.setParameter(1, String.format(LIKE_FORMAT, search));
            countQuery.setParameter(2, String.format(LIKE_FORMAT, search));
        }

        // Lấy tổng số lượng kết quả
        Long totalElements = (Long) countQuery.getSingleResult();
        log.info("totalElements={}", totalElements);

        // Tạo đối tượng Pageable để chứa thông tin phân trang
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // Tạo đối tượng Page từ danh sách kết quả và thông tin phân trang
        Page<?> page = new PageImpl<>(users, pageable, totalElements);

        // Trả về đối tượng PageResponse với thông tin phân trang và danh sách kết quả
        return PageResponse.builder()
                .page(pageNo) // Số trang hiện tại
                .size(pageSize) // Kích thước trang
                .total(page.getTotalPages())
                .totalPages(page.getTotalElements())
                .items(users) // Danh sách sinh viên
                .build();
    }


}
