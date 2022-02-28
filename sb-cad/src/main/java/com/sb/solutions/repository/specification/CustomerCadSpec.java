package com.sb.solutions.repository.specification;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.enums.CADDocumentType;
import com.sb.solutions.enums.CadDocStatus;

/**
 * @author : Rujan Maharjan on  12/7/2020
 **/
public class CustomerCadSpec implements Specification<CustomerApprovedLoanCadDocumentation> {



    private static final String FILTER_BY_BRANCH = "branchIds";
    private static final String FILTER_BY_PROVINCE = "provinceIds";

    private static final String FILTER_BY_DOC_STATUS = "docStatus";
    private static final String FILTER_BY_TO_USER = "toUser";
    private static final String FILTER_BY_TO_ROLE = "toRole";
    private static final String FILTER_BY_CUSTOMER_NAME = "name";
    private static final String FILTER_BY_CUSTOMER_TYPE = "customerType";
    private static final String FILTER_BY_CLIENT_TYPE = "clientType";


    private final String property;
    private final String value;

    CustomerCadSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerApprovedLoanCadDocumentation> root,
        CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        switch (property) {


            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join("loanHolder").get("branch").get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);

            case FILTER_BY_PROVINCE:
                Pattern pattern1 = Pattern.compile(",");
                List<Long> list1 = pattern1.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp1 = root.join("loanHolder").get("branch").get("province").get("id");
                Predicate predicate1 = exp1.in(list1);
                return criteriaBuilder.and(predicate1);

            case FILTER_BY_TO_USER:
                return criteriaBuilder
                    .equal(
                        root.join("cadCurrentStage", JoinType.LEFT).join(FILTER_BY_TO_USER).get("id"),
                        Long.valueOf(value));

            case FILTER_BY_DOC_STATUS:
                return criteriaBuilder.equal(root.get(property), CadDocStatus.valueOf(value));

            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("loanHolder").get("name")),
                        value.toLowerCase() + "%");

            case FILTER_BY_CUSTOMER_TYPE:
                return criteriaBuilder.equal(root.join("loanHolder").get("customerType"), CustomerType.valueOf(value));

            case FILTER_BY_TO_ROLE:
                return criteriaBuilder
                    .equal(
                        root.join("cadCurrentStage", JoinType.LEFT).join(FILTER_BY_TO_ROLE).get("id"),
                        Long.valueOf(value));

            case FILTER_BY_CLIENT_TYPE:
                return criteriaBuilder.equal(root.join("loanHolder").get("clientType"), ClientType.valueOf(value));


            default:
                return null;
        }
    }

}
