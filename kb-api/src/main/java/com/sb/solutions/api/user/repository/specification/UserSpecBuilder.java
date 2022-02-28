package com.sb.solutions.api.user.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.user.entity.User;

public class UserSpecBuilder {

    private final Map<String, String> params;

    public UserSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<User> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<User> spec = new UserSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new UserSpec(property, params.get(property)));
        }

        return spec;

    }
}
