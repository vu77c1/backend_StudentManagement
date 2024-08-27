package org.example.final_project.repository.specification;

import org.example.final_project.model.Student;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.example.final_project.repository.specification.SearchOperation.*;


public final class StudentSpecificationsBuilder {

    public final List<SpecSearchCriteria> params;

    public StudentSpecificationsBuilder() {
        params = new ArrayList<>();
    }

    // API
    public StudentSpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public StudentSpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            if (searchOperation == EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    searchOperation = CONTAINS;
                } else if (startWithAsterisk) {
                    searchOperation = ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchOperation = STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        }
        return this;
    }

    public Specification<Student> build() {
        if (params.isEmpty())
            return null;

        Specification<Student> result = new StudentSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new StudentSpecification(params.get(i)))
                    : Specification.where(result).and(new StudentSpecification(params.get(i)));
        }

        return result;
    }

    public StudentSpecificationsBuilder with(StudentSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public StudentSpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
