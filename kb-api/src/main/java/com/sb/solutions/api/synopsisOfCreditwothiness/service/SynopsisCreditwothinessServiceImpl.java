package com.sb.solutions.api.synopsisOfCreditwothiness.service;

import com.sb.solutions.api.synopsisOfCreditwothiness.entity.SynopsisCreditworthiness;
import com.sb.solutions.api.synopsisOfCreditwothiness.repository.SynopsisCreditworthinessRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Mohammad Hussain
 * created on 2/14/2021
 */
@Service
public class SynopsisCreditwothinessServiceImpl extends BaseServiceImpl<SynopsisCreditworthiness, Long> implements SynopsisCreditworthinessService {

    private SynopsisCreditworthinessRepository repository;

    protected SynopsisCreditwothinessServiceImpl(SynopsisCreditworthinessRepository repository) {
        super(repository);
    }

    @Override
    protected BaseSpecBuilder<SynopsisCreditworthiness> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
