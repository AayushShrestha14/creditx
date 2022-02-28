package com.sb.solutions.api.loanflag.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;

/**
 * @author Elvin Shrestha on 4/28/2020
 */
public class CustomerLoanFlagSpec implements Specification<CustomerLoanFlag> {

    public static final String FILTER_BY_CUSTOMER_LOAN_ID = "customerLoanId";

    private final String property;
    private final String value;

    public CustomerLoanFlagSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerLoanFlag> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_CUSTOMER_LOAN_ID:
                return criteriaBuilder.equal(root.join("customerLoan").get("id"),
                    Long.valueOf(value));
            default:
                return null;
        }
    }
}
