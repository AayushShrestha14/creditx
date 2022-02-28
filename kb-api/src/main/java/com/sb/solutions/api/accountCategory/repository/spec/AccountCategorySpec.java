package com.sb.solutions.api.accountCategory.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;

public class AccountCategorySpec implements Specification<AccountCategory> {

    private static final String FILTER_BY_NAME = "name";
    private final String property;
    private final String value;

    public AccountCategorySpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<AccountCategory> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(property), value + "%");
            default:
                return null;
        }
    }
}
