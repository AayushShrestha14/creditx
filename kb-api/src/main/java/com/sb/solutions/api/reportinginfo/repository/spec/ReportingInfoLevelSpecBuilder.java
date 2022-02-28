package com.sb.solutions.api.reportinginfo.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfoLevel;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 3/29/2020
 */
public class ReportingInfoLevelSpecBuilder extends BaseSpecBuilder<ReportingInfoLevel> {

    public ReportingInfoLevelSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<ReportingInfoLevel> getSpecification(String property,
        String filterValue) {
        return new ReportingInfoLevelSpec(property, filterValue);
    }
}
