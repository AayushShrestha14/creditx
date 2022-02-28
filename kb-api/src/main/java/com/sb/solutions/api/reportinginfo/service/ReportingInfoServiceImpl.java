package com.sb.solutions.api.reportinginfo.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfo;
import com.sb.solutions.api.reportinginfo.repository.ReportingInfoRepository;
import com.sb.solutions.api.reportinginfo.repository.spec.ReportingInfoSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author Elvin Shrestha on 3/27/2020
 */
@Service
public class ReportingInfoServiceImpl extends BaseServiceImpl<ReportingInfo, Long> implements
    ReportingInfoService {

    private final ReportingInfoRepository repository;

    public ReportingInfoServiceImpl(ReportingInfoRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<ReportingInfo> getSpec(Map<String, String> filterParams) {
        return new ReportingInfoSpecBuilder(filterParams);
    }
}
