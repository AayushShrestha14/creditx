package com.sb.solutions.api.nepalitemplate.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.nepalitemplate.entity.NepaliTemplate;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 1/23/2020
 */
public class NepaliTemplateSpecBuilder extends BaseSpecBuilder<NepaliTemplate> {

    public NepaliTemplateSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<NepaliTemplate> getSpecification(String property, String filterValue) {
        return new NepaliTemplateSpec(property, filterValue);
    }
}
