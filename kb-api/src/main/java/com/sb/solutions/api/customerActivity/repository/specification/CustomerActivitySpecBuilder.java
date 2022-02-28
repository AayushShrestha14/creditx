package com.sb.solutions.api.customerActivity.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author : Rujan Maharjan on  9/19/2020
 **/
public class CustomerActivitySpecBuilder extends BaseSpecBuilder<CustomerActivity> {

    public CustomerActivitySpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CustomerActivity> getSpecification(String property,
        String filterValue) {
        return new CustomerActivitySpec(property, filterValue);
    }
}
