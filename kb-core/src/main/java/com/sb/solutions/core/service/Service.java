package com.sb.solutions.core.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Generic Service Interface
 *
 * Methods that are generic in multiple scenarios will be added into it and
 * implemented into BaseServiceImpl
 *
 * @param <T> Entity
 * @param <I> type of Entity ID
 *
 * example: MemoService
 */
public interface Service<T, I> {

    T save(T t);

    List<T> saveAll(List<T> entities);

    Optional<T> findOne(I i);

    List<T> findAll();

    void delete(T entity);

    void deleteById(I i);

    Page<T> findPageableBySpec(Map<String, String> filterParams, Pageable pageable);

    List<T> findAllBySpec(Map<String, String> filterParams);

    Optional<T> findOneBySpec(Map<String, String> filterParams);
}
