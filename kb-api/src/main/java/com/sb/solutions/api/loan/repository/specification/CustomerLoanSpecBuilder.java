package com.sb.solutions.api.loan.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loan.entity.CustomerLoan;

/**
 * @author Rujan Maharjan on 6/8/2019
 */
public class CustomerLoanSpecBuilder {

    private final Map<String, String> params;


    public CustomerLoanSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<CustomerLoan> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<CustomerLoan> spec = new CustomerLoanSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new CustomerLoanSpec(property, params.get(property)));
        }

        return spec;
    }
}
