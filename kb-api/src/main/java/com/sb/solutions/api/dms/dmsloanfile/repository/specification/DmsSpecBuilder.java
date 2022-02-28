package com.sb.solutions.api.dms.dmsloanfile.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;

public class DmsSpecBuilder {

    private final Map<String, String> params;

    public DmsSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<DmsLoanFile> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }
        final List<String> properites = new ArrayList<>(params.keySet());
        final String firstProperty = properites.get(0);

        Specification<DmsLoanFile> spec = new DmsSpec(properites.get(0), params.get(firstProperty));
        for (int i = 1; i < properites.size(); i++) {
            final String property = properites.get(i);
            spec = Specification.where(spec).and(new DmsSpec(property, params.get(property)));
        }
        return spec;
    }

}
