package com.sb.solutions.api.sector.sector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.sector.sector.entity.Sector;
import com.sb.solutions.api.sector.sector.repository.SectorRepository;
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
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;

    @Override
    public Map<Object, Object> sectorStatusCount() {
        return sectorRepository.sectorStatusCount();
    }

    @Override
    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    @Override
    public Sector findOne(Long id) {
        return sectorRepository.getOne(id);
    }

    @Override
    public Sector save(Sector sector) {
        sector.setLastModifiedAt(new Date());
        if (sector.getId() == null) {
            sector.setStatus(Status.ACTIVE);
        }
        return sectorRepository.save(sector);
    }

    @Override
    public Page<Sector> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return sectorRepository.sectorFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<Sector> saveAll(List<Sector> list) {
        return sectorRepository.saveAll(list);
    }
}
