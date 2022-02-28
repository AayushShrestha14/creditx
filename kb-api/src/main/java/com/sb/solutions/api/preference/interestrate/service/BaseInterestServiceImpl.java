package com.sb.solutions.api.preference.interestrate.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.preference.interestrate.entity.BaseInterestRate;
import com.sb.solutions.api.preference.interestrate.repository.BaseInterestRepository;
import com.sb.solutions.core.enums.Status;

@Service
public class BaseInterestServiceImpl implements BaseInterestService {
    public final BaseInterestRepository baseInterestRepository;

    @Autowired
    public BaseInterestServiceImpl(BaseInterestRepository baseInterestRepository) {
        this.baseInterestRepository = baseInterestRepository;
    }

    @Override
    public List<BaseInterestRate> findAll() {
        return baseInterestRepository.findAll();
    }

    @Override
    public BaseInterestRate findOne(Long id) {
        return baseInterestRepository.getOne(id);
    }

    @Override
    public BaseInterestRate save(BaseInterestRate baseInterestRate) {
        BaseInterestRate previousInterestValue = baseInterestRepository.findFirstByOrderByIdDesc();
        if (previousInterestValue != null) {
            previousInterestValue.setStatus(Status.INACTIVE);
            baseInterestRepository.save(previousInterestValue);
        }
        baseInterestRate.setStatus(Status.ACTIVE);
        return baseInterestRepository.save(baseInterestRate);
    }

    @Override
    public Page<BaseInterestRate> findAllPageable(Object t, Pageable pageable) {
        return baseInterestRepository.findAllByOrderByIdDesc(pageable);
    }

    @Override
    public List<BaseInterestRate> saveAll(List<BaseInterestRate> list) {
        return baseInterestRepository.saveAll(list);
    }


    @Override
    public BaseInterestRate getActiveRate() {
        return baseInterestRepository.findAllByStatus(Status.ACTIVE);
    }
}
