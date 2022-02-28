package com.sb.solutions.api.address.municipalityVdc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.municipalityVdc.repository.MunicipalityVdcRepository;
import com.sb.solutions.core.dto.SearchDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MunicipalityVdcServiceImpl implements MunicipalityVdcService {

    private final MunicipalityVdcRepository municipalityVdcRepository;

    @Override
    public List<MunicipalityVdc> findAll() {
        return municipalityVdcRepository.findAll();
    }

    @Override
    public MunicipalityVdc findOne(Long id) {
        return municipalityVdcRepository.getOne(id);
    }

    @Override
    public MunicipalityVdc save(MunicipalityVdc municipalityVdc) {
        return municipalityVdcRepository.save(municipalityVdc);
    }

    @Override
    public Page<MunicipalityVdc> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return municipalityVdcRepository
            .municipalityVdcFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<MunicipalityVdc> saveAll(List<MunicipalityVdc> list) {
        return municipalityVdcRepository.saveAll(list);
    }

    @Override
    public List<MunicipalityVdc> findAllByDistrict(District district) {
        return municipalityVdcRepository.findAllByDistrict(district);
    }
}
