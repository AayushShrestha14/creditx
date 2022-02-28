package com.sb.solutions.api.customer.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.entity.Customer;

public class CustomerSpecBuilder {

    private final Map<String, String> params;


    public CustomerSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<Customer> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<Customer> spec = new CustomerSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new CustomerSpec(property, params.get(property)));
        }

        return spec;
    }

}
