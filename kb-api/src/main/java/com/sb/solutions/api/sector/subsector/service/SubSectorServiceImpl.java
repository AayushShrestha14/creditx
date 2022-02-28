package com.sb.solutions.api.sector.subsector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.sector.subsector.entity.SubSector;
import com.sb.solutions.api.sector.subsector.repository.SubSectorRepository;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SubSectorServiceImpl implements SubSectorService {

    private final SubSectorRepository subSectorRepository;


    @Override
    public List<SubSector> findAll() {
        return subSectorRepository.findAll();
    }

    @Override
    public SubSector findOne(Long id) {
        return subSectorRepository.getOne(id);
    }

    @Override
    public SubSector save(SubSector subSector) {
        subSector.setLastModifiedAt(new Date());
        if (subSector.getId() == null) {
            subSector.setStatus(Status.ACTIVE);
        }
        return subSectorRepository.save(subSector);
    }

    @Override
    public Page<SubSector> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return subSectorRepository
            .subSectorFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<SubSector> saveAll(List<SubSector> list) {
        return subSectorRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> subSectorStatusCount() {
        return subSectorRepository.subSectorStatusCount();
    }
}
