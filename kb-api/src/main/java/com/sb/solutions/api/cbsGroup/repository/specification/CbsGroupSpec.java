package com.sb.solutions.api.cbsGroup.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.cbsGroup.entity.CbsGroup;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/
public class CbsGroupSpec implements Specification<CbsGroup> {

    private static final String FILTER_BY_CBS_OBLIGOR = "obligor";
    private final String property;
    private final String value;

    public CbsGroupSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CbsGroup> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_CBS_OBLIGOR:
                return criteriaBuilder.equal(root.get(property), value);
        }
        return null;
    }
}
