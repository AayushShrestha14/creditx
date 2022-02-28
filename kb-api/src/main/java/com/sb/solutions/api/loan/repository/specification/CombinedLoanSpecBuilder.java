package com.sb.solutions.api.loan.repository.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loan.entity.CombinedLoan;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
public class CombinedLoanSpecBuilder extends BaseSpecBuilder<CombinedLoan> {

    public CombinedLoanSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<CombinedLoan> getSpecification(String property, String filterValue) {
        return new CombinedLoanSpec(property, filterValue);
    }
}
