package com.sb.solutions.repository.specification.customerLoan;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author : Rujan Maharjan on  12/28/2020
 **/
public class CadCustomerLoanSpecBuilder extends BaseSpecBuilder<CustomerLoan> {

    public CadCustomerLoanSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CustomerLoan> getSpecification(String property, String filterValue) {
        return new CadCustomerLoanSpec(property, filterValue);
    }
}
