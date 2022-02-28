package com.sb.solutions.api.fiscalyear.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.fiscalyear.entity.FiscalYear;
import com.sb.solutions.api.fiscalyear.repository.FiscalYearRepository;

@Service
@Transactional
public class FiscalYearServiceImpl implements FiscalYearService {

    private final FiscalYearRepository fiscalYearRepository;
    protected FiscalYearServiceImpl(
        @Autowired FiscalYearRepository repository) {
        this.fiscalYearRepository = repository;
    }

    @Override
    public List<FiscalYear> findAll() {
        return fiscalYearRepository.findAllByOrderByYear();
    }

    @Override
    public FiscalYear findOne(Long id) {
        return fiscalYearRepository.getOne(id);
    }

    @Override
    public FiscalYear save(FiscalYear fiscalYear) {
        if(fiscalYear.getIsCurrentYear()){
            this.deActiveFiscalYears();
        }
        return fiscalYearRepository.save(fiscalYear);
    }

    @Override
    public Page<FiscalYear> findAllPageable(Object t, Pageable pageable) {
        return fiscalYearRepository.findAll(pageable);
    }

    @Override
    public List<FiscalYear> saveAll(List<FiscalYear> list) {
        return fiscalYearRepository.saveAll(list);
    }

    @Override
    public void deActiveFiscalYears() {
        fiscalYearRepository.deActivePreviousFiscalYear();
    }
}
