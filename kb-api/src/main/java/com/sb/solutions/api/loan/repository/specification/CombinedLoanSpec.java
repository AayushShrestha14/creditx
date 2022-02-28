package com.sb.solutions.api.loan.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loan.entity.CombinedLoan;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
public class CombinedLoanSpec implements Specification<CombinedLoan> {

    private final String property;
    private final String value;

    public CombinedLoanSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CombinedLoan> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
