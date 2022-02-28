package com.sb.solutions.api.openingForm.repository.specification;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.openingForm.entity.OpeningForm;
import com.sb.solutions.core.enums.AccountStatus;

public class OpeningFormSpec implements Specification<OpeningForm> {

    private static final String FILTER_BY_ACCOUNT_STATUS = "status";
    private static final String FILTER_BY_BRANCH = "branch";

    private final String property;
    private final String value;

    public OpeningFormSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<OpeningForm> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_ACCOUNT_STATUS:
                return criteriaBuilder.equal(root.get(property), AccountStatus.valueOf(value));

            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join(property).get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);

            default:
                return null;
        }
    }
}
