package com.sb.solutions.api.address.province.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.address.province.repository.ProvinceRepository;
import com.sb.solutions.core.dto.SearchDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Override
    public List<Province> findAll() {
        return provinceRepository.findAll();
    }

    @Override
    public Province findOne(Long id) {
        return provinceRepository.getOne(id);
    }

    @Override
    public Province save(Province province) {
        return provinceRepository.save(province);
    }

    @Override
    public Page<Province> findAllPageable(Object object, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        SearchDto s = objectMapper.convertValue(object, SearchDto.class);
        return provinceRepository.provinceFilter(s.getName() == null ? "" : s.getName(), pageable);
    }

    @Override
    public List<Province> saveAll(List<Province> list) {
        return provinceRepository.saveAll(list);
    }
}
