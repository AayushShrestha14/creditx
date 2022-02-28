package com.sb.solutions.api.nepseCompany.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.core.enums.Status;

public class NepseSpec implements Specification<NepseCompany> {

    private static final String FILTER_BY_STATUS = "status";

    private final String property;
    private final String value;

    NepseSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<NepseCompany> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
            default:
                return null;
        }
    }
}
