package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgGroup;
import com.sb.solutions.api.crg.repository.CrgGroupRepository;
import com.sb.solutions.api.crg.repository.specification.CrgGroupSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */

@Service
public class CrgGroupServiceImpl extends BaseServiceImpl<CrgGroup, Long> implements CrgGroupService {

    private final CrgGroupRepository crgGroupRepository;

    public CrgGroupServiceImpl(
            CrgGroupRepository repository) {
        super(repository);
        this.crgGroupRepository = repository;
    }

    @Override
    public Page<CrgGroup> findPageable(Map<String, String> filterParams, Pageable pageable) {
        final CrgGroupSpecBuilder builder = new CrgGroupSpecBuilder(filterParams);
        final Specification<CrgGroup> spec = builder.build();
        return crgGroupRepository.findAll(spec, pageable);
    }

    @Override
    protected BaseSpecBuilder<CrgGroup> getSpec(Map<String, String> filterParams) {
        return new CrgGroupSpecBuilder(filterParams);
    }
}
