package com.sb.solutions.api.memo.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.enums.Stage;
import com.sb.solutions.core.enums.Status;

public class MemoSpec implements Specification<Memo> {

    private static final String FILTER_BY_SUBJECT = "subject";
    private static final String FILTER_BY_STATUS = "status";
    private static final String FILTER_BY_STAGE = "stage";
    private static final String FILTER_BY_TYPE = "type.name";
    private static final String FILTER_BY_TYPE_ID = "type.id";
    private static final String FILTER_BY_SENDER = "sentBy.id";
    private static final String FILTER_BY_SENDER_NAME = "sentBy.name";
    private static final String FILTER_BY_RECEIVER = "sentTo.id";
    private static final String FILTER_BY_RECEIVER_NAME = "sentTo.name";
    private static final String FILTER_BY_STAGES_RECEIVER = "stages.sentTo.id";

    private final String property;
    private final String value;

    public MemoSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Memo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (property) {
            case FILTER_BY_SUBJECT:
                return criteriaBuilder.like(root.get(property), value + "%");
            case FILTER_BY_STAGE:
                return criteriaBuilder.equal(root.get(property), Stage.valueOf(value));
            case FILTER_BY_STATUS:
                return criteriaBuilder.equal(root.get(property), Status.valueOf(value));
            case FILTER_BY_TYPE:
                return criteriaBuilder.like(root.join("type").get("name"), value + "%");
            case FILTER_BY_TYPE_ID:
                return criteriaBuilder.equal(root.join("type").get("id"), Long.parseLong(value));
            case FILTER_BY_SENDER:
                return criteriaBuilder.equal(root.join("sentBy").get("id"), Long.parseLong(value));
            case FILTER_BY_SENDER_NAME:
                return criteriaBuilder.like(root.join("sentBy").get("name"), value + "%");
            case FILTER_BY_RECEIVER:
                return criteriaBuilder.equal(root.join("sentTo").get("id"), Long.parseLong(value));
            case FILTER_BY_RECEIVER_NAME:
                return criteriaBuilder.like(root.join("sentTo").get("name"), value + "%");
            case FILTER_BY_STAGES_RECEIVER:
                return criteriaBuilder
                    .equal(root.join("stages").get("sentTo").get("id"), Long.parseLong(value));
            default:
                return null;
        }
    }
}
