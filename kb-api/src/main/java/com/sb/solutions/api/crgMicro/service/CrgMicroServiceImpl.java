package com.sb.solutions.api.crgMicro.service;

import com.sb.solutions.api.crgMicro.entity.CrgMicro;
import com.sb.solutions.api.crgMicro.repository.CrgMicroRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Amulya Shrestha on 5/23/2021
 **/

@Service
@AllArgsConstructor
public class CrgMicroServiceImpl implements CrgMicroService {

    private final CrgMicroRepository crgMicroRepository;

    @Override
    public List<CrgMicro> findAll() {
        return this.crgMicroRepository.findAll();
    }

    @Override
    public CrgMicro findOne(Long id) {
        return this.crgMicroRepository.getOne(id);
    }

    @Override
    public CrgMicro save(CrgMicro crgMicro) {
        return this.crgMicroRepository.save(crgMicro);
    }

    @Override
    public Page<CrgMicro> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CrgMicro> saveAll(List<CrgMicro> list) {
        return this.crgMicroRepository.saveAll(list);
    }
}
