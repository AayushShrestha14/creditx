package com.sb.solutions.api.valuator.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.valuator.entity.Valuator;
import com.sb.solutions.api.valuator.repository.ValuatorRepository;
import com.sb.solutions.api.valuator.repository.spec.ValuatorSpecBuilder;
import com.sb.solutions.core.enums.Status;

@Service
@AllArgsConstructor
public class ValuatorServiceImpl implements ValuatorService {

    private final ValuatorRepository valuatorRepository;

    @Override
    public List<Valuator> findAll() {
        return valuatorRepository.findAll();
    }

    @Override
    public Valuator findOne(Long id) {
        return valuatorRepository.getOne(id);
    }

    @Override
    public Valuator save(Valuator valuator) {
        valuator.setLastModifiedAt(new Date());
        if (valuator.getId() == null) {
            valuator.setStatus(Status.ACTIVE);
        }
        return valuatorRepository.save(valuator);
    }

    @Override
    public Page<Valuator> findAllPageable(Object object, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(object, Map.class);
        final ValuatorSpecBuilder builder = new ValuatorSpecBuilder(s);
        final Specification<Valuator> specification = builder.build();
        return valuatorRepository.findAll(specification, pageable);
    }

    @Override
    public List<Valuator> saveAll(List<Valuator> list) {
        return valuatorRepository.saveAll(list);
    }

    @Override
    public Collection<Valuator> getValuatorFilterBySearch(Object search) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(search, Map.class);
        final ValuatorSpecBuilder builder = new ValuatorSpecBuilder(s);
        final Specification<Valuator> specification = builder.build();
        return valuatorRepository.findAll(specification);
    }

    @Override
    public Map<Object, Object> valuatorStatusCount() {
        return valuatorRepository.valuatorStatusCount();
    }
}
