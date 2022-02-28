package com.sb.solutions.core.service;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Sunil Babu Shrestha on 12/31/2018
 */
public interface BaseService<T> {

    /**
     *
     */
    List<T> findAll();


    default List<T> findAll(Object search) {
        return Collections.emptyList();
    }

    /**
     *
     */
    T findOne(Long id);

    /**
     *
     */
    T save(T t);

    /**
     *
     */
    Page<T> findAllPageable(Object t, Pageable pageable);

    /**
     *
     */
    List<T> saveAll(List<T> list);


}
