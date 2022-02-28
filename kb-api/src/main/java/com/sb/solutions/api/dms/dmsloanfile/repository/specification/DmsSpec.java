package com.sb.solutions.api.dms.dmsloanfile.repository.specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;


public class DmsSpec implements Specification<DmsLoanFile> {

    private static final String FILTER_BY_DATE = "createdAt";
    private static final String FILTER_BY_NAME = "customerName";
    private static final String FILTER_BY_LOAN = "loanConfigId";
    private final String property;
    private String value;

    public DmsSpec(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<DmsLoanFile> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        Date date;
        switch (property) {
            case FILTER_BY_DATE:
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(value);
                    return criteriaBuilder.equal(root.get(property), date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return null;
                }
            case FILTER_BY_NAME:
                return criteriaBuilder.like(root.get(property), value + "%");
            case FILTER_BY_LOAN:
                return criteriaBuilder.and(
                    criteriaBuilder.equal(root.join("loanConfig").get("id"), Long.valueOf(value)));
            default:
                return null;
        }
    }
}
