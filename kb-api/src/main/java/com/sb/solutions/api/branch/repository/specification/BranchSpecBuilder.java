package com.sb.solutions.api.branch.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.branch.entity.Branch;

/**
 * @author Rujan Maharjan on 5/1/2019
 */
public class BranchSpecBuilder {

    private final Map<String, String> params;


    public BranchSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<Branch> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<Branch> spec = new BranchSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new BranchSpec(property, params.get(property)));
        }

        return spec;
    }
}
