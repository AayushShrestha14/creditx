package com.sb.solutions.api.borrowerPortfolio.service;

import com.sb.solutions.api.borrowerPortfolio.BorrowerPortFolioRepo;
import com.sb.solutions.api.borrowerPortfolio.entity.BorrowerPortFolio;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class BorrowerPortFolioServiceImpl extends BaseServiceImpl<BorrowerPortFolio , Long> implements BorrowerPortFolioService{
    private final BorrowerPortFolioRepo repository;

    public BorrowerPortFolioServiceImpl(BorrowerPortFolioRepo borrowerPortFolioRepo) {
        super(borrowerPortFolioRepo);
        this.repository = borrowerPortFolioRepo;
    }

    @Override
    protected BaseSpecBuilder<BorrowerPortFolio> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
