package com.sb.solutions.api.financial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.financial.entity.Financial;
import com.sb.solutions.api.financial.repository.FinancialRepository;

@Service
public class FinancialServiceImpl implements FinancialService {

    FinancialRepository financialRepository;

    @Autowired
    public FinancialServiceImpl(FinancialRepository financialRepository) {
        this.financialRepository = financialRepository;
    }

    @Override
    public List<Financial> findAll() {
        return financialRepository.findAll();
    }

    @Override
    public Financial findOne(Long id) {
        return financialRepository.getOne(id);
    }

    @Override
    public Financial save(Financial financial) {
        return financialRepository.save(financial);
    }

    @Override
    public Page<Financial> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Financial> saveAll(List<Financial> list) {
        return financialRepository.saveAll(list);
    }
}
