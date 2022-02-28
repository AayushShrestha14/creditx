package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgGamma;
import com.sb.solutions.api.crg.repository.CrgGammaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Amulya Shrestha on 9/19/2020
 **/

@Service
@AllArgsConstructor
public class CrgGammaServiceImpl implements CrgGammaService {
    private final CrgGammaRepository crgGammaRepository;

    @Override
    public List<CrgGamma> findAll() {
        return crgGammaRepository.findAll();
    }

    @Override
    public CrgGamma findOne(Long id) {
        return crgGammaRepository.getOne(id);
    }

    @Override
    public CrgGamma save(CrgGamma crgGamma) {
        return crgGammaRepository.save(crgGamma);
    }

    @Override
    public Page<CrgGamma> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CrgGamma> saveAll(List<CrgGamma> list) {
        return crgGammaRepository.saveAll(list);
    }
}
