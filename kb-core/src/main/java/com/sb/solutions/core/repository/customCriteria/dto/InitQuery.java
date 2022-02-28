package com.sb.solutions.core.repository.customCriteria.dto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lombok.Data;

/**
 * @author : Rujan Maharjan on  5/10/2021
 **/
@Data
public class InitQuery<E, D> {

    private CriteriaBuilder criteriaBuilder;
    private Root<E> root;
    private CriteriaQuery<D> criteriaQuery;

}
