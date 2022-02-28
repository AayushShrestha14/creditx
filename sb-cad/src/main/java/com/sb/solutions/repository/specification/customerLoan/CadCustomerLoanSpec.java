package com.sb.solutions.repository.specification.customerLoan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.gson.Gson;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.enums.DocStatus;

/**
 * @author : Rujan Maharjan on  12/28/2020
 **/
public class CadCustomerLoanSpec implements Specification<CustomerLoan> {

    public static final String FILTER_BY_TO_USER = "toUser";
    public static final String FILTER_BY_BRANCH = "branchIds";
    public static final String FILTER_BY_LOAN = "loanConfigId";
    public static final String FILTER_BY_DOC_STATUS = "documentStatus";
    public static final String FILTER_BY_CURRENT_USER_ROLE = "currentUserRole";
    private final String property;
    private final String value;

    CadCustomerLoanSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerLoan> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {

            case FILTER_BY_DOC_STATUS:
                if (value.equalsIgnoreCase("initial")) {
                    List<DocStatus> list = new ArrayList<>();
                    list.add(DocStatus.DISCUSSION);
                    list.add(DocStatus.DOCUMENTATION);
                    list.add(DocStatus.UNDER_REVIEW);
                    list.add(DocStatus.VALUATION);
                    Expression<String> exp = root.get(property);
                    Predicate predicate = exp.in(list);
                    return criteriaBuilder.and(predicate);
                } else {
                    return criteriaBuilder.equal(root.get(property), DocStatus.valueOf(value));
                }

            case FILTER_BY_LOAN:
                return criteriaBuilder
                    .and(criteriaBuilder.equal(root.join("loan").get("id"), Long.valueOf(value)));

            case FILTER_BY_CURRENT_USER_ROLE:
                return criteriaBuilder
                    .equal(root.join("currentStage", JoinType.LEFT).join("toRole").get("id"),
                        Long.valueOf(value));

            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join("branch").get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);


            case FILTER_BY_TO_USER:
                return criteriaBuilder
                    .equal(
                        root.join("currentStage", JoinType.LEFT).join(FILTER_BY_TO_USER).get("id"),
                        Long.valueOf(value));
            default:
                return null;
        }
    }
}
