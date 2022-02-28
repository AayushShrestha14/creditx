package com.sb.solutions.api.microbaselriskexposure.service;

import com.sb.solutions.api.microbaselriskexposure.entity.MicroBaselRiskExposure;
import com.sb.solutions.api.microbaselriskexposure.repository.MicroBaselRiskExposureRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Mohammad Hussain
 * created on 2/16/2021
 */
@Service
public class MicroBaselRiskExposureServiceImpl extends BaseServiceImpl<MicroBaselRiskExposure, Long> implements MicroBaselRiskExposureService {

    private MicroBaselRiskExposureRepository repository;

    protected MicroBaselRiskExposureServiceImpl(MicroBaselRiskExposureRepository repository) {
        super(repository);
    }

    @Override
    protected BaseSpecBuilder<MicroBaselRiskExposure> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
