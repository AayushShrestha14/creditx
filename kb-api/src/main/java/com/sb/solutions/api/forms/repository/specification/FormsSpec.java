package com.sb.solutions.api.forms.repository.specification;

import com.sb.solutions.api.forms.entity.Forms;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author : Rujan Maharjan on  11/7/2020
 **/
public class FormsSpec implements Specification<Forms> {

    private final String property;
    private final String value;

    public FormsSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }
    @Override
    public Predicate toPredicate(Root<Forms> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        return null;
    }
}
