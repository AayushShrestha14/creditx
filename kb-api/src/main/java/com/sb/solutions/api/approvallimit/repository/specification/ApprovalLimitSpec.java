package com.sb.solutions.api.approvallimit.repository.specification;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Sunil Babu Shrestha on 2/24/2021
 */
public class ApprovalLimitSpec implements Specification<ApprovalLimit> {

    private static final String FILTER_BY_LOAN_CATEGORIES = "loanCategory";
    private static final String FILTER_BY_AUTHORITIES = "authorities";
    private static final String FILTER_BY_APPROVAL_TYPE = "loanApprovalType";

    private final String property;
    private final String value;

    public ApprovalLimitSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<ApprovalLimit> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (property){
            case FILTER_BY_AUTHORITIES:
                return criteriaBuilder.equal(root.join(FILTER_BY_AUTHORITIES).get("id"),Long.valueOf(value));
            case FILTER_BY_APPROVAL_TYPE:
                return criteriaBuilder.equal(root.get(property), LoanApprovalType.valueOf(value));
            case FILTER_BY_LOAN_CATEGORIES:
                return criteriaBuilder.equal(root.join(FILTER_BY_LOAN_CATEGORIES).get("id"),Long.valueOf(value));
            default:
                return null;
        }
    }
}
