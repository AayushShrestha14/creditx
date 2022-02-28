package com.sb.solutions.api.loanflag.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 4/28/2020
 */
public class CustomerLoanFlagSpecBuilder extends BaseSpecBuilder<CustomerLoanFlag> {

    public CustomerLoanFlagSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CustomerLoanFlag> getSpecification(String property,
        String filterValue) {
        return new CustomerLoanFlagSpec(property, filterValue);
    }
}
