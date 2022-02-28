package com.sb.solutions.api.loan.repository.specification;

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

import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanFlag;
import com.sb.solutions.core.enums.LoanTag;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.PostApprovalAssignStatus;

/**
 * @author Rujan Maharjan on 6/8/2019
 */
public class CustomerLoanSpec implements Specification<CustomerLoan> {

    public static final String FILTER_BY_LOAN = "loanConfigId";
    public static final String FILTER_BY_DOC_STATUS = "documentStatus";
    public static final String FILTER_BY_CURRENT_USER_ROLE = "currentUserRole";
    public static final String FILTER_BY_TO_USER = "toUser";
    public static final String FILTER_BY_BRANCH = "branchIds";
    public static final String FILTER_BY_CURRENT_STAGE_DATE = "currentStageDate";
    public static final String FILTER_BY_TYPE = "loanNewRenew";
    public static final String FILTER_BY_NOTIFY = "notify";
    public static final String FILTER_BY_CUSTOMER_NAME = "customerName";
    public static final String FILTER_BY_COMPANY_NAME = "companyName";
    public static final String FILTER_BY_DOC_ACTION = "docAction";
    public static final String FILTER_BY_CUSTOMER_RELATIVE_NAME = "customerRelativeName";
    public static final String FILTER_BY_CUSTOMER_CITIZENSHIP = "citizenshipNumber";
    public static final String FILTER_BY_CUSTOMER_CITIZEN_ISSUE_DATE = "citizenshipIssuedDate";

    public static final String FILTER_BY_GUARANTOR_NAME = "guarantorName";
    public static final String FILTER_BY_GUARANTOR_CITIZENSHIP = "guarantorCitizenshipNumber";
    public static final String FILTER_BY_GUARANTOR_CITIZEN_ISSUE_DATE = "guarantorCitizenshipIssuedDate";
    public static final String FILTER_BY_GUARANTOR_DISTRICT_ID = "guarantorDistrictId";
    public static final String FILTER_BY_GUARANTOR_PROVINCE_ID = "guarantorProvinceId";

    public static final String FILTER_BY_SHARE_LOAN_EXCEEDING_LIMIT = "showShareLoanExcessingLimit";
    public static final String FILTER_BY_INSURANCE_EXPIRY = "isInsuranceExpired";
    public static final String FILTER_BY_HAS_INSURANCE = "hasInsurance";
    public static final String FILTER_BY_IS_CLOSE_RENEW = "isCloseRenew";
    public static final String FILTER_BY_IS_NOT_COMBINED = "isNotCombined";
    public static final String FILTER_BY_LOAN_HOLDER_ID = "loanHolderId";
    public static final String FILTER_BY_IS_STAGED = "isStaged";
    private static final String FILTER_BY_CUSTOMER_GROUP_CODE = "groupCode";
    private static final String FILTER_BY_LOAN_ASSIGNED_TO_USER = "postApprovalAssignStatus";
    private static final String FILTER_BY_POST_APPROVAL_CURRENT_USER = "postApprovalAssignedUser";
    private static final String FILTER_BY_NOT_IN_LOAN_IDS = "notLoanIds";
    private static final String FILTER_BY_BRANCH_PROVINCE_ID = "provinceId";
    public static final String FILTER_BY_NAME = "name";
    public static final String FILTER_BY_CUSTOMER_TYPE = "customerType";
    public static final String FILTER_BY_USER = "users";
    public static final String FILTER_BY_BUSINESS_UNIT = "clientType";
    public static final String FILTER_BY_LOAN_HOLDER_CODE = "customerCode";
    public static final String FILTER_BY_LOAN_HOLDER_ID_IN = "loanHolderIdIn";

    public static final String FILTER_BY_LOAN_TAG = "loanTag";
    private final String property;
    private final String value;

    public CustomerLoanSpec(String property, String value) {
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

            case FILTER_BY_CURRENT_STAGE_DATE:
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

            case FILTER_BY_TO_USER:
                return criteriaBuilder
                    .equal(
                        root.join("currentStage", JoinType.LEFT).join(FILTER_BY_TO_USER).get("id"),
                        Long.valueOf(value));

            case FILTER_BY_TYPE:
                return criteriaBuilder.equal(root.get("loanType"), LoanType.valueOf(value));

            case FILTER_BY_NOTIFY:
                Predicate notifyPredicate = criteriaBuilder
                    .equal(root.get(FILTER_BY_NOTIFY), Boolean.valueOf(value));
                Predicate notedByPredicate = criteriaBuilder.isNull(root.get("notedBy"));
                return criteriaBuilder.and(notifyPredicate, notedByPredicate);

            case FILTER_BY_CUSTOMER_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("loanHolder").get("name")),
                        value.toLowerCase() + "%");

//            case FILTER_BY_COMPANY_NAME:
//                return criteriaBuilder
//                    .like(criteriaBuilder
//                            .lower(root.join("companyInfo").get(FILTER_BY_COMPANY_NAME)),
//                        "%" + value.toLowerCase() + "%");

            case FILTER_BY_DOC_ACTION:
                return criteriaBuilder
                    .and(criteriaBuilder.equal(root.get("currentStage").get("docAction"),
                        DocAction.valueOf(value)));

//
//            case FILTER_BY_CURRENT_OFFER_LETTER_STAGE:
//
//                Predicate currentOfferLetterStage = criteriaBuilder
//                    .equal(
//                        root.join("customerOfferLetter", JoinType.LEFT)
//                            .join("offerLetterStage", JoinType.LEFT)
//                            .join("toUser", JoinType.LEFT).get("id"),
//                        Long.valueOf(value));
//                Predicate noCurrentOfferLetterStage = criteriaBuilder
//                    .isNull(root.get("customerOfferLetter"));
//                Predicate approvedOfferLetter = criteriaBuilder
//                    .equal(
//                        root.join("customerOfferLetter", JoinType.LEFT)
//                            .get("docStatus"),
//                        DocStatus.valueOf(DocStatus.APPROVED.name()));
//                return criteriaBuilder
//                    .or(noCurrentOfferLetterStage, currentOfferLetterStage,approvedOfferLetter);

            case FILTER_BY_CUSTOMER_RELATIVE_NAME:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("customerInfo").join("customerRelatives").get(property),
                            value));

            case FILTER_BY_CUSTOMER_CITIZENSHIP:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("customerInfo").join("customerRelatives").get(property),
                            value));

            case FILTER_BY_CUSTOMER_CITIZEN_ISSUE_DATE:
                try {
                    return criteriaBuilder
                        .and(criteriaBuilder
                            .equal(
                                root.join("customerInfo").join("customerRelatives").get(property),
                                new SimpleDateFormat("yyyy-MM-dd").parse(value)));
                } catch (ParseException e) {
                    return null;
                }

            case FILTER_BY_GUARANTOR_NAME:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("guarantor").join("guarantorList").get("name"),
                            value));

            case FILTER_BY_GUARANTOR_CITIZENSHIP:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(root.join("guarantor").join("guarantorList").get("citizenNumber"),
                            value));

            case FILTER_BY_GUARANTOR_CITIZEN_ISSUE_DATE:
                try {
                    return criteriaBuilder
                        .and(criteriaBuilder
                            .equal(
                                root.join("guarantor").join("guarantorList").get("issuedYear"),
                                new SimpleDateFormat("yyyy-MM-dd").parse(value)));
                } catch (ParseException e) {
                    return null;
                }

            case FILTER_BY_GUARANTOR_DISTRICT_ID:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(
                            root.join("guarantor").join("guarantorList").join("district").get("id"),
                            value));

            case FILTER_BY_GUARANTOR_PROVINCE_ID:
                return criteriaBuilder
                    .and(criteriaBuilder
                        .equal(
                            root.join("guarantor").join("guarantorList").join("province").get("id"),
                            value));

            case FILTER_BY_SHARE_LOAN_EXCEEDING_LIMIT:
                Predicate idEqual = criteriaBuilder.
                    equal(root.join("loanHolder").join("loanFlags").get("customerLoanId"),
                        root.get("id"));
                Predicate shareFlagExist = criteriaBuilder.equal(root.join("loanHolder").
                    join("loanFlags").get("flag"), LoanFlag.INSUFFICIENT_SHARE_AMOUNT);
                Predicate hasShareLoanTag = criteriaBuilder.equal(root.join("loan").get("loanTag"),
                    LoanTag.SHARE_SECURITY);
                return criteriaBuilder
                    .and(idEqual, shareFlagExist, hasShareLoanTag);

            case FILTER_BY_INSURANCE_EXPIRY:
                return criteriaBuilder.equal(root.join("loanHolder").join("loanFlags").get("flag"),
                    LoanFlag.INSURANCE_EXPIRY);

            case FILTER_BY_HAS_INSURANCE:
                return criteriaBuilder.and(criteriaBuilder.isMember(AppConstant.TEMPLATE_INSURANCE,
                    root.join("loan").get("templateList")));

            case FILTER_BY_IS_CLOSE_RENEW:
                Predicate notNull = criteriaBuilder.isNotNull(root.get(FILTER_BY_IS_CLOSE_RENEW));

                Predicate isNull = criteriaBuilder.isNull(root.get(FILTER_BY_IS_CLOSE_RENEW));
                Predicate equal = criteriaBuilder
                    .equal(root.get(FILTER_BY_IS_CLOSE_RENEW), Boolean.valueOf(value));
                return Boolean.parseBoolean(value) ? criteriaBuilder.and(notNull, equal)
                    : criteriaBuilder.or(isNull, equal);

            case FILTER_BY_IS_NOT_COMBINED:
                return criteriaBuilder.isNull(root.get("combinedLoan"));
            case FILTER_BY_LOAN_HOLDER_ID:
                return criteriaBuilder
                    .equal(root.join("loanHolder").get("id"), Long.valueOf(value));

            case FILTER_BY_IS_STAGED:
                boolean isStaged = Boolean.parseBoolean(value);
                Expression<?> ex = root.get("previousStageList");
                return isStaged ? criteriaBuilder.isNotNull(ex) : criteriaBuilder.isNull(ex);

            case FILTER_BY_CUSTOMER_GROUP_CODE:
                return criteriaBuilder
                    .equal(root.join("loanHolder").join("customerGroup").get("groupCode"), value);

            case FILTER_BY_LOAN_ASSIGNED_TO_USER:
                if (PostApprovalAssignStatus.NOT_ASSIGNED
                    .equals(PostApprovalAssignStatus.valueOf(value))) {
                    Predicate predicateNotAssigned = criteriaBuilder
                        .equal(root.get(property), PostApprovalAssignStatus.valueOf(value));
                    Predicate predicateNUll = criteriaBuilder.isNull(root.get(property));
                    return criteriaBuilder.or(predicateNotAssigned, predicateNUll);
                } else {
                    return criteriaBuilder
                        .equal(root.get(property), PostApprovalAssignStatus.valueOf(value));
                }

            case FILTER_BY_POST_APPROVAL_CURRENT_USER:
                return criteriaBuilder
                    .equal(root.join("postApprovalAssignedUser").get("id"), Long.valueOf(value));

            case FILTER_BY_NOT_IN_LOAN_IDS:
                Pattern pattern1 = Pattern.compile(",");
                List<Long> list1 = pattern1.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp1 = root.get("id");
                Predicate predicate1 = exp1.in(list1).not();
                return criteriaBuilder.and(predicate1);

            case FILTER_BY_BRANCH_PROVINCE_ID:
                Pattern pattern2 = Pattern.compile(",");
                List<Long> list2 = pattern2.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp2 = root.join("loanHolder").get("branch").get("province")
                    .get("id");
                Predicate predicate2 = exp2.in(list2);
                return criteriaBuilder.and(predicate2);

            case FILTER_BY_NAME:
                return criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.join("loanHolder").get("name")),
                        value.toLowerCase() + "%");

            case FILTER_BY_CUSTOMER_TYPE:
                return criteriaBuilder.equal(root.join("loanHolder").get("customerType"),
                    CustomerType.valueOf(value));

            case FILTER_BY_USER:
                return criteriaBuilder
                    .like(root.join("currentStage", JoinType.LEFT).join("toUser").get("name"),
                        value.toLowerCase() + "%");

            case FILTER_BY_BUSINESS_UNIT:
                return criteriaBuilder
                    .equal(root.join("loanHolder").get("clientType"), ClientType.valueOf(value));

            case FILTER_BY_LOAN_HOLDER_CODE:
                return criteriaBuilder
                    .like(root.join("loanHolder", JoinType.LEFT).get(property),
                        value.toLowerCase() + "%");
            case FILTER_BY_LOAN_TAG:
                return  criteriaBuilder.equal(root.join("loan").get("loanTag") , LoanTag.valueOf(value));

            case FILTER_BY_LOAN_HOLDER_ID_IN:
                Pattern pattern11 = Pattern.compile(",");
                List<Long> list11= pattern11.splitAsStream(value)
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
                Expression<String> exp11 = root.get("loanHolder").get("id");
                Predicate predicate11 = exp11.in(list11);
                return criteriaBuilder.and(predicate11);

            default:
                return null;
        }
    }


}
