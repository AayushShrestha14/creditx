package com.sb.solutions.api.insurance.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.insurance.entity.Insurance;

/**
 * @author Elvin Shrestha on 4/12/2020
 */
public class InsuranceSpec implements Specification<Insurance> {

    private final String property;
    private final String value;

    public InsuranceSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Insurance> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
