package com.sb.solutions.api.customerGroup.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.core.repository.BaseSpecBuilder;

public class CustomerGroupSpecBuilder extends BaseSpecBuilder<CustomerGroup> {

    public CustomerGroupSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CustomerGroup> getSpecification(String property, String filterValue) {
        return new CustomerGroupSpec(property, filterValue);
    }
}
