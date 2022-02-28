package com.sb.solutions.api.crg.repository.specification;

import com.sb.solutions.api.crg.entity.CrgGroup;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

/**
 * @author Amulya Shrestha on 9/14/2020
 **/
public class CrgGroupSpecBuilder extends BaseSpecBuilder<CrgGroup> {

    public CrgGroupSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CrgGroup> getSpecification(String property, String filterValue) {
        return new CrgGroupSpec(property, filterValue);
    }
}
