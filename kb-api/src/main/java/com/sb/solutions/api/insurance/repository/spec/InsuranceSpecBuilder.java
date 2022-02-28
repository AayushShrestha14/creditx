package com.sb.solutions.api.insurance.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 4/12/2020
 */
public class InsuranceSpecBuilder extends BaseSpecBuilder<Insurance> {

    public InsuranceSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<Insurance> getSpecification(String property, String filterValue) {
        return new InsuranceSpec(property, filterValue);
    }
}
