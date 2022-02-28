package com.sb.solutions.api.cbsGroup.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.cbsGroup.entity.CbsGroup;
import com.sb.solutions.api.customer.repository.specification.CustomerInfoSpec;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/
public class CbsGroupSpecBuilder extends BaseSpecBuilder<CbsGroup> {

    public CbsGroupSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CbsGroup> getSpecification(String property, String filterValue) {
        return new CbsGroupSpec(property, filterValue);
    }
}
