package com.sb.solutions.api.customerActivity.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;

/**
 * @author : Rujan Maharjan on  9/19/2020
 **/
public class CustomerActivitySpec implements Specification<CustomerActivity> {

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_ACTIVITY = "activity";
    private static final String FILTER_BY_CUSTOMER_TYPE = "customerType";
    private static final String FILTER_BY_PROFILE_ID = "profileId";
    private final String property;
    private final String value;

    public CustomerActivitySpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerActivity> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("profile").get(FILTER_BY_NAME)),
                        value.toLowerCase() + "%");
            case FILTER_BY_ACTIVITY:
                return criteriaBuilder.equal(root.get(FILTER_BY_ACTIVITY), Activity.valueOf(value));

            case FILTER_BY_CUSTOMER_TYPE:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.get("profile").get(FILTER_BY_CUSTOMER_TYPE),
                            CustomerType.valueOf(value)
                        ));

            case FILTER_BY_PROFILE_ID:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("profile").get("id"),
                            Long.valueOf(value)
                        ));
            default:
                return null;
        }
    }
}
