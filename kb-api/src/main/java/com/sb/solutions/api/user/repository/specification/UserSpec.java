package com.sb.solutions.api.user.repository.specification;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.Status;

public class UserSpec implements Specification<User> {

    private static final String FILTER_BY_NAME = "name";
    private static final String FILTER_BY_USER_NAME = "username";
    private static final String FILTER_BY_EMAIL = "email";
    private static final String FILTER_BY_BRANCH = "branchIds";
    private static final String FILTER_BY_ROLE = "roleId";
    private static final String FILTER_BY_STATUS = "status";

    private final String property;
    private final String value;

    public UserSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(property), value + "%");
            case FILTER_BY_EMAIL:
                return criteriaBuilder
                        .equal(criteriaBuilder.lower(root.get(property)), value.toLowerCase() + "%");
            case FILTER_BY_USER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder.lower(root.get(property)), value.toLowerCase() + "%");

            case FILTER_BY_BRANCH:
                if (value.equalsIgnoreCase(RoleAccess.ALL.toString())) {
                    return criteriaBuilder
                        .and(criteriaBuilder
                            .equal(root.join("role").get("roleAccess"), RoleAccess.valueOf(value)));
                } else {
                    Pattern pattern = Pattern.compile(",");
                    List<Long> list = pattern.splitAsStream(value)
                        .map(Long::valueOf)
                        .collect(Collectors.toList());
                    Expression<String> exp = root.join("branch").get("id");
                    Predicate predicate = exp.in(list);
                    return criteriaBuilder.and(predicate);
                }
            case FILTER_BY_ROLE:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("role").get("id"), Long.valueOf(value)));
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
            default:
                return null;
        }
    }
}
