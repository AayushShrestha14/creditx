package com.sb.solutions.api.eligibility.applicant.repository.specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.google.gson.Gson;
import org.springframework.data.jpa.domain.Specification;

import com.sb.solutions.api.eligibility.applicant.entity.Applicant;
import com.sb.solutions.api.eligibility.common.EligibilityStatus;

public class ApplicantSpecification implements Specification<Applicant> {

    private static final String FILTER_BY_ELIGIBILITY_STATUS = "eligibilityStatus";
    private static final String FILTER_BY_DATE = "dateFilter";
    private static final String FILTER_BY_LOAN = "loanConfigId";
    private static final String FILTER_BY_BRANCH = "branchIds";

    private final String property;
    private final String value;


    public ApplicantSpecification(String property, String value) {
        this.property = property;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Applicant> root, CriteriaQuery<?> criteriaQuery,
        CriteriaBuilder criteriaBuilder) {
        switch (property) {
            case FILTER_BY_ELIGIBILITY_STATUS:
                Pattern pattern = Pattern.compile(",");
                List<EligibilityStatus> list = pattern.splitAsStream(value)
                    .map(EligibilityStatus::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp = root.get("eligibilityStatus");
                Predicate predicate = exp.in(list);
                return criteriaBuilder.and(predicate);
            case FILTER_BY_DATE:
                Gson gson = new Gson();
                Map dates = gson.fromJson(value, Map.class);
                try {
                    return criteriaBuilder.between(root.get("createdAt"),
                        new SimpleDateFormat("MM/dd/yyyy")
                            .parse(String.valueOf(dates.get("startDate"))),
                        new SimpleDateFormat("MM/dd/yyyy")
                            .parse(String.valueOf(dates.get("endDate"))));
                } catch (ParseException e) {
                    return null;
                }
            case FILTER_BY_LOAN:
                return criteriaBuilder.equal(root.join("loanConfig").get("id"),
                    Long.valueOf(String.valueOf(value)));
            case FILTER_BY_BRANCH:
                return criteriaBuilder.equal(root.join("branch").get("id"),
                    Long.valueOf(String.valueOf(value)));
            default:
                return null;
        }
    }
}
