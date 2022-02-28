package com.sb.solutions.api.reportinginfo.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfoLevel;
import com.sb.solutions.api.reportinginfo.repository.ReportingInfoLevelRepository;
import com.sb.solutions.api.reportinginfo.repository.spec.ReportingInfoLevelSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author Elvin Shrestha on 3/29/2020
 */
@Service
public class ReportingInfoLevelServiceImpl extends
    BaseServiceImpl<ReportingInfoLevel, Long> implements ReportingInfoLevelService {

    private final ReportingInfoLevelRepository repository;

    public ReportingInfoLevelServiceImpl(
        ReportingInfoLevelRepository repository) {
        super(repository);

        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<ReportingInfoLevel> getSpec(Map<String, String> filterParams) {
        return new ReportingInfoLevelSpecBuilder(filterParams);
    }
}
