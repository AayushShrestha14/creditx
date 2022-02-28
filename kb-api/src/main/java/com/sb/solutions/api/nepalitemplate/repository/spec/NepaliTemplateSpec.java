package com.sb.solutions.api.nepalitemplate.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.nepalitemplate.entity.NepaliTemplate;

/**
 * @author Elvin Shrestha on 1/23/2020
 */
public class NepaliTemplateSpec implements Specification<NepaliTemplate> {

    private static final String FILTER_BY_CUSTOMER_LOAN_ID = "customerLoan.id";

    private final String property;
    private final String value;

    public NepaliTemplateSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<NepaliTemplate> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_CUSTOMER_LOAN_ID:
                return criteriaBuilder
                    .equal(root.join("customerLoan").get("id"), Long.valueOf(value));
            default:
                return null;
        }
    }
}
