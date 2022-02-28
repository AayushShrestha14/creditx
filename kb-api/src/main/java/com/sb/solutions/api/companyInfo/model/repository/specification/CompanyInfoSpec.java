package com.sb.solutions.api.companyInfo.model.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;

public class CompanyInfoSpec implements Specification<CompanyInfo> {

    private static final String FILTER_BY_REGISTRATION_NUMBER = "registrationNumber";
    private final String property;
    private final String value;

    public CompanyInfoSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CompanyInfo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_REGISTRATION_NUMBER:
                return criteriaBuilder.equal(root.get(property), value);
            default:
                return null;
        }
    }
}
