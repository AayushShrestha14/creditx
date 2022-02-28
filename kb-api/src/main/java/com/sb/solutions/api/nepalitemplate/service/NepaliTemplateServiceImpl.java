package com.sb.solutions.api.nepalitemplate.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.nepalitemplate.entity.NepaliTemplate;
import com.sb.solutions.api.nepalitemplate.repository.NepaliTemplateRepository;
import com.sb.solutions.api.nepalitemplate.repository.spec.NepaliTemplateSpecBuilder;

/**
 * @author Elvin Shrestha on 1/23/2020
 */
@Service
public class NepaliTemplateServiceImpl implements NepaliTemplateService {

    private final NepaliTemplateRepository repository;

    public NepaliTemplateServiceImpl(
        NepaliTemplateRepository repository) {
        this.repository = repository;
    }

    @Override
    public NepaliTemplate save(NepaliTemplate nepaliTemplate) {
        return repository.save(nepaliTemplate);
    }

    @Override
    public List<NepaliTemplate> saveAll(List<NepaliTemplate> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public Optional<NepaliTemplate> findOne(Long aLong) {
        return Optional.of(repository.getOne(aLong));
    }

    @Override
    public List<NepaliTemplate> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(NepaliTemplate entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Override
    public Page<NepaliTemplate> findPageableBySpec(Map<String, String> filterParams,
        Pageable pageable) {
        NepaliTemplateSpecBuilder builder = new NepaliTemplateSpecBuilder(filterParams);
        Specification<NepaliTemplate> specification = builder.build();
        return repository.findAll(specification, pageable);
    }

    @Override
    public List<NepaliTemplate> findAllBySpec(Map<String, String> filterParams) {
        NepaliTemplateSpecBuilder builder = new NepaliTemplateSpecBuilder(filterParams);
        Specification<NepaliTemplate> specification = builder.build();
        return repository.findAll(specification);
    }

    @Override
    public Optional<NepaliTemplate> findOneBySpec(Map<String, String> filterParams) {
        NepaliTemplateSpecBuilder builder = new NepaliTemplateSpecBuilder(filterParams);
        Specification<NepaliTemplate> specification = builder.build();
        return repository.findOne(specification);
    }
}
