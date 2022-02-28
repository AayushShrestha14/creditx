package com.sb.solutions.api.reportinginfo.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfo;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 3/27/2020
 */
public class ReportingInfoSpecBuilder extends BaseSpecBuilder<ReportingInfo> {

    public ReportingInfoSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<ReportingInfo> getSpecification(String property, String filterValue) {
        return new ReportingInfoSpec(property, filterValue);
    }
}
