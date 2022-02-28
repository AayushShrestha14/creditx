package com.sb.solutions.api.preference.notificationMaster.repository.spec;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.core.enums.NotificationMasterType;

/**
 * @author Elvin Shrestha on 4/12/2020
 */
public class NotificationMasterSpec implements Specification<NotificationMaster> {

    public static final String FILTER_BY_NOTIFICATION_KEY = "notificationKey";

    private final String property;
    private final String value;

    public NotificationMasterSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<NotificationMaster> root, CriteriaQuery<?> query,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NOTIFICATION_KEY:
                return criteriaBuilder.equal(root.get(FILTER_BY_NOTIFICATION_KEY),
                    NotificationMasterType.valueOf(value));
            default:
                return null;
        }
    }
}
