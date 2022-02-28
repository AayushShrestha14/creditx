package com.sb.solutions.core.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.core.repository.BaseRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * Base implementation of generic service
 * All the sub-classes can override the available methods where generic operation does
 * not satisfy
 *
 * @param <T> Entity
 * @param <I> type of Entity ID
 *
 * example: MemoServiceImpl
 */
public abstract class BaseServiceImpl<T, I> implements Service<T, I> {

    private final BaseRepository<T, I> repository;

    protected BaseServiceImpl(
        BaseRepository<T, I> repository) {
        this.repository = repository;
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(I i) {
        repository.deleteById(i);
    }

    @Override
    public Page<T> findPageableBySpec(Map<String, String> filterParams, Pageable pageable) {
        final BaseSpecBuilder<T> builder = getSpec(filterParams);
        final Specification<T> spec = builder.build();

        return repository.findAll(spec, pageable);
    }

    @Override
    public List<T> findAllBySpec(Map<String, String> filterParams) {
        final BaseSpecBuilder<T> builder = getSpec(filterParams);
        final Specification<T> spec = builder.build();

        return repository.findAll(spec);
    }

    @Override
    public Optional<T> findOneBySpec(Map<String, String> filterParams) {
        final BaseSpecBuilder<T> builder = getSpec(filterParams);
        final Specification<T> spec = builder.build();

        return repository.findOne(spec);
    }

    @Override
    public Optional<T> findOne(I i) {
        return repository.findById(i);
    }

    @Override
    public T save(T t) {
        return repository.save(t);
    }

    @Override
    public List<T> saveAll(List<T> list) {
        return repository.saveAll(list);
    }

    protected abstract BaseSpecBuilder<T> getSpec(Map<String, String> filterParams);

}
