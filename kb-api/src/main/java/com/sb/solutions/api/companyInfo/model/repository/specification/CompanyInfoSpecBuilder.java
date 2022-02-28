package com.sb.solutions.api.companyInfo.model.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;

public class CompanyInfoSpecBuilder {

    private final Map<String, String> params;

    public CompanyInfoSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<CompanyInfo> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<CompanyInfo> spec = new CompanyInfoSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new CompanyInfoSpec(property, params.get(property)));
        }

        return spec;
    }
}
