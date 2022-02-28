package com.sb.solutions.core.repository.customCriteria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author : Rujan Maharjan on  5/9/2021
 **/

@Data
@AllArgsConstructor
public class CriteriaDto<E, D> {

    private Class<E> rootClass;
    private Class<D> dtoClass;
    private Specification<E> specification;
    private String[] columns;
    private String[] joinColumn;

}
