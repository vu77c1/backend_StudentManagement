package org.example.final_project.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.final_project.model.Student;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
public class StudentSpecification implements Specification<Student> {
    private SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull final Root<Student> root, @NonNull final CriteriaQuery<?> query, @NonNull final CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN ->
                    builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN ->
                    builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE ->
                    builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH ->
                    builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS ->
                    builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }
}
