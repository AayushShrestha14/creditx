package com.sb.solutions.api.microBorrowerFinancial.service;

import com.sb.solutions.api.microBorrowerFinancial.MicroBorrowerFinancial;
import com.sb.solutions.api.microBorrowerFinancial.repository.MicroBorrowerFinancialRepo;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MicroBorrowerFinancialServiceImpl extends BaseServiceImpl<MicroBorrowerFinancial , Long> implements MicroBorrowerFinancialService {

    private MicroBorrowerFinancialRepo repository;

    protected MicroBorrowerFinancialServiceImpl(MicroBorrowerFinancialRepo repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<MicroBorrowerFinancial> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
