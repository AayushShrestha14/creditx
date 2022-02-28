package com.sb.solutions.api.creditRiskGrading.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.creditRiskGrading.entity.CreditRiskGrading;
import com.sb.solutions.api.creditRiskGrading.repository.CreditRiskGradingRepository;

/**
 * @author yunish on 11/25/2019
 */
@Service
@AllArgsConstructor
public class CreditRiskGradingServiceImpl implements CreditRiskGradingService {

    private final CreditRiskGradingRepository creditRiskGradingRepository;

    @Override
    public List<CreditRiskGrading> findAll() {
        return creditRiskGradingRepository.findAll();
    }

    @Override
    public CreditRiskGrading findOne(Long id) {
        return creditRiskGradingRepository.getOne(id);
    }

    @Override
    public CreditRiskGrading save(CreditRiskGrading creditRiskGrading) {
        return creditRiskGradingRepository.save(creditRiskGrading);
    }

    @Override
    public Page<CreditRiskGrading> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CreditRiskGrading> saveAll(List<CreditRiskGrading> list) {
        return creditRiskGradingRepository.saveAll(list);
    }
}
