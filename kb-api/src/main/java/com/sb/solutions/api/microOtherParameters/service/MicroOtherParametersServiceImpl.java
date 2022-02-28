package com.sb.solutions.api.microOtherParameters.service;

import com.sb.solutions.api.microOtherParameters.MicroOtherParameters;
import com.sb.solutions.api.microOtherParameters.repository.MicroOtherParametersRepo;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Amulya Shrestha
 **/

@Service
public class MicroOtherParametersServiceImpl extends BaseServiceImpl<MicroOtherParameters, Long> implements MicroOtherParametersService {
    private MicroOtherParametersRepo repository;

    protected MicroOtherParametersServiceImpl(MicroOtherParametersRepo repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<MicroOtherParameters> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
