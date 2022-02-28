package com.sb.solutions.api.customer.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
public class CustomerInfoSpecBuilder extends BaseSpecBuilder<CustomerInfo> {

    public CustomerInfoSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CustomerInfo> getSpecification(String property, String filterValue) {
        return new CustomerInfoSpec(property, filterValue);
    }
}
