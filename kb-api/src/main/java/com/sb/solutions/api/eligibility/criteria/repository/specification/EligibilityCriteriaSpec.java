package com.sb.solutions.api.eligibility.criteria.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.core.enums.Status;

public class EligibilityCriteriaSpec implements Specification<EligibilityCriteria> {

    private static final String FILTER_BY_STATUS = "status";
    private static final String FILTER_BY_STATUS_NOT = "statusNot";

    private final String property;
    private final String value;

    public EligibilityCriteriaSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<EligibilityCriteria> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
            case FILTER_BY_STATUS_NOT:
                return criteriaBuilder.notEqual(root.get(property), Status.valueOf(value));
            default:
                return null;
        }
    }
}
