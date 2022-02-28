package com.sb.solutions.api.eligibility.applicant.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.eligibility.applicant.entity.Applicant;

public class ApplicantSpecificationBuilder {

    private final Map<String, String> params;


    public ApplicantSpecificationBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<Applicant> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<Applicant> spec = new ApplicantSpecification(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new ApplicantSpecification(property, params.get(property)));
        }

        return spec;
    }
}
