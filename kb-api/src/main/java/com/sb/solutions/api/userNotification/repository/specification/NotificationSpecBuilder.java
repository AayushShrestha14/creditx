package com.sb.solutions.api.userNotification.repository.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.userNotification.entity.Message;

public class NotificationSpecBuilder {

    private final Map<String, String> params;

    public NotificationSpecBuilder(Map<String, String> params) {
        this.params = params;
    }

    public Specification<Message> build() {
        if (MapUtils.isEmpty(params)) {
            return null;
        }

        final List<String> properties = new ArrayList<>(params.keySet());

        final String firstProperty = properties.get(0);

        Specification<Message> spec = new NotificationSpec(properties.get(0),
            params.get(firstProperty));

        for (int i = 1; i < properties.size(); i++) {
            final String property = properties.get(i);
            spec = Specification.where(spec)
                .and(new NotificationSpec(property, params.get(property)));
        }

        return spec;
    }
}
