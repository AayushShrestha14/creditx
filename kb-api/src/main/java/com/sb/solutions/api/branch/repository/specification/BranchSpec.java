package com.sb.solutions.api.branch.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.branch.entity.Branch;

/**
 * @author Rujan Maharjan on 5/1/2019
 */
public class BranchSpec implements Specification<Branch> {

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_PROVINCE = "provinceId";
    private static final String FILTER_BY_DISTRICT = "districtId";
    private static final String FILTER_BY_MUNCIPALTIY = "municipalityId";
    private static final String FILTER_BY_STREET_ADDRESS = "streetName";
    private static final String FILTER_BY_WARD_NUMBER = "wardNumber";
    private static final String FILTER_BY_CREATED_BY = "createdBy";
    private static final String FILTER_BY_BRANCH_CODE = "branchCode";
    private final String property;
    private final String value;

    public BranchSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }


    @Override
    public Predicate toPredicate(Root<Branch> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {

            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(property), value + "%");

            case FILTER_BY_STREET_ADDRESS:
                return criteriaBuilder.like(root.get(property), value + "%");

            case FILTER_BY_PROVINCE:
                return criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("province").get("id"), Long.valueOf(value)));

            case FILTER_BY_DISTRICT:
                return criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("district").get("id"), Long.valueOf(value)));

            case FILTER_BY_MUNCIPALTIY:
                return criteriaBuilder.and(criteriaBuilder
                    .equal(root.join("municipalityVdc").get("id"), Long.valueOf(value)));

            case FILTER_BY_BRANCH_CODE:
                return criteriaBuilder.like(root.get(property), value);

            default:
                return null;
        }
    }

}
