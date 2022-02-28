package com.sb.solutions.api.memo.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.enums.Status;

public class MemoTypeSpec implements Specification<MemoType> {

    private final String propertyName;
    private final String propertyValue;

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_STATUS = "status";
    private static final String FILTER_BY_CREATED_AT = "createdAt";
    private static final String FILTER_BY_LAST_MODIFIED_AT = "lastModifiedAt";

    MemoTypeSpec(String propertyName, String propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Override
    public Predicate toPredicate(Root<MemoType> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (propertyName) {
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(propertyName), propertyValue + "%");
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(propertyName), Status.valueOf(propertyValue));
            default:
                return null;
        }
    }
}
