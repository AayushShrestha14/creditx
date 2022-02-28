package com.sb.solutions.api.valuator.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.valuator.entity.Valuator;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 1/2/20
 */
public class ValuatorSpecBuilder extends BaseSpecBuilder<Valuator> {

    public ValuatorSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<Valuator> getSpecification(String property, String filterValue) {
        return new ValuatorSpec(property, filterValue);
    }
}
