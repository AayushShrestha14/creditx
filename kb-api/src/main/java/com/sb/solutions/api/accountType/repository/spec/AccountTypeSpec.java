package com.sb.solutions.api.accountType.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.accountType.entity.AccountType;

public class AccountTypeSpec implements Specification<AccountType> {

    private static final String FILTER_BY_NAME = "name";
    private final String property;
    private final String value;

    public AccountTypeSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<AccountType> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(property), value + "%");
            default:
                return null;
        }
    }
}
