package com.sb.solutions.api.creditRiskGradingAlpha.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.sb.solutions.api.creditRiskGradingAlpha.entity.CreditRiskGradingAlpha;
import com.sb.solutions.api.creditRiskGradingAlpha.repository.CreditRiskGradingAlphaRepository;

/**
 * @author Amulya Shrestha on 7/30/2020
 **/

@Service
public class CreditRiskGradingAlphaServiceImpl implements CreditRiskGradingAlphaService {

    CreditRiskGradingAlphaRepository creditRiskGradingAlphaRepository;

    @Autowired
    public CreditRiskGradingAlphaServiceImpl(CreditRiskGradingAlphaRepository creditRiskGradingAlphaRepository) {
        this.creditRiskGradingAlphaRepository = creditRiskGradingAlphaRepository;
    }

    @Override
    public List<CreditRiskGradingAlpha> findAll() {
        return creditRiskGradingAlphaRepository.findAll();
    }

    @Override
    public CreditRiskGradingAlpha findOne(Long id) {
        return creditRiskGradingAlphaRepository.getOne(id);
    }

    @Override
    public CreditRiskGradingAlpha save(CreditRiskGradingAlpha creditRiskGradingAlpha) {
        return creditRiskGradingAlphaRepository.save(creditRiskGradingAlpha);
    }

    @Override
    public Page<CreditRiskGradingAlpha> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CreditRiskGradingAlpha> saveAll(List<CreditRiskGradingAlpha> list) {
        return creditRiskGradingAlphaRepository.saveAll(list);
    }
}
