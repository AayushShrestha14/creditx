package com.sb.solutions.api.crg.repository.specification;

import com.sb.solutions.api.crg.entity.CrgGroup;
import com.sb.solutions.core.enums.Status;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Amulya Shrestha on 9/14/2020
 **/
public class CrgGroupSpec implements Specification<CrgGroup> {

    private final String propName;
    private final String propValue;

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_STATUS = "status";
    private static final String FILTER_BY_CREATED_AT = "createdAt";
    private static final String FILTER_BY_LAST_MODIFIED_AT = "lastModifiedAt";

    CrgGroupSpec(String propertyName, String propertyValue) {
        this.propName = propertyName;
        this.propValue = propertyValue;
    }

    @Override
    public Predicate toPredicate(Root<CrgGroup> root,
                                 CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {

        switch (propName) {
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(propName), Status.valueOf(propValue));
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(propName), propValue + "%");
            default:
                return null;
        }
    }
}
