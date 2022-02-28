package com.sb.solutions.api.nepseCompany.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.core.repository.BaseSpecBuilder;

public class NepseSpecBuilder extends BaseSpecBuilder<NepseCompany> {

    public NepseSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<NepseCompany> getSpecification(String property, String filterValue) {
        return new NepseSpec(property, filterValue);
    }
}
