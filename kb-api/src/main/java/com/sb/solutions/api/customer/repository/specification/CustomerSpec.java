package com.sb.solutions.api.customer.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.entity.Customer;

public class CustomerSpec implements Specification<Customer> {

    private static final String FILTER_BY_CITIZENSHIP_NUMBER = "citizenshipNumber";
    private static final String FILTER_BY_CUSTOMER_NAME = "customerName";

    private final String property;
    private final String value;

    public CustomerSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (property) {
            case FILTER_BY_CITIZENSHIP_NUMBER:
                return criteriaBuilder.equal(root.get(property), value);

            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get(property)),
                        value.toLowerCase() + "%");
            default:
                return null;
        }
    }
}
