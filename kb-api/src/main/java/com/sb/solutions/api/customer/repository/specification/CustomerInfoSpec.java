package com.sb.solutions.api.customer.repository.specification;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerIdType;
import com.sb.solutions.api.customer.enums.CustomerType;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
public class CustomerInfoSpec implements Specification<CustomerInfo> {

    private static final String FILTER_BY_CUSTOMER_NAME = "name";
    private static final String FILTER_BY_CUSTOMER_TYPE = "customerType";
    private static final String FILTER_BY_CUSTOMER_ID_TYPE = "idType";
    private static final String FILTER_BY_CUSTOMER_ID_NUMBER = "idNumber";
    private static final String FILTER_BY_BRANCH = "branchIds";
    private static final String FILTER_BY_ID_REG_PLACE = "idRegPlace";
    private static final String FILTER_BY_GROUP_ID = "groupId";
    private static final String FILTER_BY_BRANCH_PROVINCE_ID = "provinceId";
    private final String property;
    private final String value;

    public CustomerInfoSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<CustomerInfo> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {

        switch (property) {

            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get(property)),
                        value.toLowerCase() + "%");

            case FILTER_BY_CUSTOMER_TYPE:
                return criteriaBuilder.equal(root.get(property), CustomerType.valueOf(value));

            case FILTER_BY_CUSTOMER_ID_TYPE:
                return criteriaBuilder.equal(root.get(property), CustomerIdType.valueOf(value));

            case FILTER_BY_CUSTOMER_ID_NUMBER:
                return criteriaBuilder.equal(root.get(property), value.trim());

            case FILTER_BY_ID_REG_PLACE:
                return criteriaBuilder.equal(root.get(property), value);

            case FILTER_BY_BRANCH:
                Pattern pattern = Pattern.compile(",");
                List<Long> list = pattern.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.join("branch").get("id");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);

            case FILTER_BY_BRANCH_PROVINCE_ID:
                Pattern pattern1 = Pattern.compile(",");
                List<Long> list1 = pattern1.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp1 = root.join("branch").get("province").get("id");
                Predicate predicate1 = exp1.in(list1);
                return criteriaBuilder.and(predicate1);

            case FILTER_BY_GROUP_ID:
                return criteriaBuilder
                    .equal(root.join("customerGroup").get("id"), Long.valueOf(value));
            default:
                return null;
        }
    }
}



