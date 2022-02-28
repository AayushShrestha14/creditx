package com.sb.solutions.api.preference.notificationMaster.repository.spec;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.core.repository.BaseSpecBuilder;

/**
 * @author Elvin Shrestha on 4/12/2020
 */
public class NotificationMasterSpecBuilder extends BaseSpecBuilder<NotificationMaster> {

    public NotificationMasterSpecBuilder(Map<String, String> params) {
        super(params);
    }

    @Override
    protected Specification<NotificationMaster> getSpecification(String property,
        String filterValue) {
        return new NotificationMasterSpec(property, filterValue);
    }
}
