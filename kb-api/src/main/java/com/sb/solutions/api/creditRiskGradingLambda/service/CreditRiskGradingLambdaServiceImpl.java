package com.sb.solutions.api.creditRiskGradingLambda.service;

import com.sb.solutions.api.creditRiskGradingLambda.entity.CreditRiskGradingLambda;
import com.sb.solutions.api.creditRiskGradingLambda.repository.CreditRiskGradingLambdaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Amulya Shrestha on 11/8/2020
 **/

@Service
@AllArgsConstructor
public class CreditRiskGradingLambdaServiceImpl implements CreditRiskGradingLambdaService {

    private final CreditRiskGradingLambdaRepository creditRiskGradingLambdaRepository;

    @Override
    public List<CreditRiskGradingLambda> findAll() {
        return this.creditRiskGradingLambdaRepository.findAll();
    }

    @Override
    public CreditRiskGradingLambda findOne(Long id) {
        return this.creditRiskGradingLambdaRepository.getOne(id);
    }

    @Override
    public CreditRiskGradingLambda save(CreditRiskGradingLambda creditRiskGradingLambda) {
        return this.creditRiskGradingLambdaRepository.save(creditRiskGradingLambda);
    }

    @Override
    public Page<CreditRiskGradingLambda> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CreditRiskGradingLambda> saveAll(List<CreditRiskGradingLambda> list) {
        return this.creditRiskGradingLambdaRepository.saveAll(list);
    }
}
