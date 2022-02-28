package com.sb.solutions.api.userNotification.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.userNotification.entity.Message;
import com.sb.solutions.core.enums.Status;

public class NotificationSpec implements Specification<Message> {

    private static final String FILTER_BY_STATUS = "status";
    private static final String FILTER_BY_USER_ID = "toId";
    private static final String FILTER_BY_USER_ROLE = "toRole";

    private final String property;
    private final String value;

    public NotificationSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
            case FILTER_BY_USER_ID:
                return criteriaBuilder.equal(root.get(property), this.value);
            case FILTER_BY_USER_ROLE:
                return criteriaBuilder.equal(root.get(property), this.value);
            default:
                return null;
        }
    }
}
