package com.sb.solutions.api.customerActivity.aop;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.enums.ActivityType;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.string.StringUtil;

/**
 * @author : Rujan Maharjan on  9/22/2020
 **/
@Aspect
@Component
@Slf4j
public class CustomerLoanAspect {

    private static final String DESCRIPTION_UPDATE = "%s of %s has been updated Successfully";
    private static final String DESCRIPTION_NEW = "%s : %s has been raised Successfully";
    private final ActivityService activityService;
    private final CustomerLoanService customerLoanService;
    private final LoanConfigService loanConfigService;
    private final UserService userService;
    private ObjectMapper mapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));

    public CustomerLoanAspect(
        ActivityService activityService,
        CustomerLoanService customerLoanService,
        LoanConfigService loanConfigService,
        UserService userService) {
        this.activityService = activityService;
        this.customerLoanService = customerLoanService;
        this.loanConfigService = loanConfigService;
        this.userService = userService;
    }

    private static List<Object> getDifference(Object s1, Object s2) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (Field field : s1.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value1 = field.get(s1);
            Object value2 = field.get(s2);
            if (value1 != null && value2 != null) {
                if (!Objects.equals(value1, value2)) {
                    Map<String, Object> map = new HashMap<>();
                    String a = CaseFormat.UPPER_CAMEL
                        .to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                    map.put("fieldName", StringUtil
                        .getStringWithWhiteSpaceAndWithAllFirstLetterCapitalize(
                            a.replace("_", " ")));
                    map.put("prevValue", value1);
                    values.add(map);
                }
            }
        }
        return values;
    }

    private static boolean getDifferenceBetweenSet(Set<Guarantor> newData,
        Set<Guarantor> oldData) {
        if (newData.isEmpty() && oldData.isEmpty()) {
            return false;
        } else if (newData.size() != oldData.size()) {
            return true;
        } else {
            List<Long> newList = newData.stream().map(Guarantor::getId)
                .collect(Collectors.toList());
            List<Long> oldList = newData.stream().map(Guarantor::getId)
                .collect(Collectors.toList());
            return !newList.containsAll(oldList);
        }

    }


    @Pointcut("execution(* com.sb.solutions..*.*(..))")
    public void serviceLayer() {
    }

    /**
     * This is for  Loan Activity purpose only
     *
     * @param batch
     * @param customerLoanLog
     **/
    @Around(value = "serviceLayer() && @annotation(customerLoanLog) && args(batch,..)")
    public Object trackAroundCustomerLoan(ProceedingJoinPoint pjp, Object batch,
        CustomerLoanLog customerLoanLog)
        throws Throwable {
        Long id = null;
        CustomerLoan customerLoanPrev;
        List<String> propertyList = new ArrayList<>();
        String changedProperty = null;
        String data = null;
        String description = null;
        CustomerActivity customerActivity = new CustomerActivity();
        if (batch instanceof CustomerLoan) {
            CustomerLoan c = (CustomerLoan) batch;

            switch (customerLoanLog.value()) {
                case LOAN_UPDATE:
                    String loanName = loanConfigService.findOne(c.getLoan().getId()).getName();
                    String loanType = c.getLoanType().name();
                    if (!ObjectUtils.isEmpty(c.getId())) {
                        id = c.getId();
                        Map<String, Object> diff = new LinkedHashMap<>();

                        customerLoanPrev = customerLoanService.findOne(c.getId());

                        loanName = customerLoanPrev.getLoan().getName();
                        ProposalData prevProposal = mapper
                            .readValue(customerLoanPrev.getProposal().getData(),
                                ProposalData.class);
                        ProposalData newProposal = mapper
                            .readValue(c.getProposal().getData(), ProposalData.class);
                        List<Object> proposalChanges = getDifference(prevProposal, newProposal);
                        if (!proposalChanges.isEmpty()) {
                            propertyList.add("PROPOSAL");
                        }
                        diff.put("proposal", proposalChanges);
                        boolean isChanged = getDifferenceBetweenSet(c.getTaggedGuarantors(),
                            customerLoanPrev.getTaggedGuarantors());
                        if (isChanged) {
                            diff.put("guarantor", customerLoanPrev.getTaggedGuarantors());
                            propertyList.add("GUARANTOR");
                        }
                        if (!customerLoanPrev.getPriority().equals(c.getPriority())) {
                            diff.put("priority", customerLoanPrev.getPriority());
                            propertyList.add("PRIORITY");
                        }
                        if (!customerLoanPrev.getDocumentStatus().equals(c.getDocumentStatus())) {
                            diff.put("documentStatus", customerLoanPrev.getDocumentStatus());
                            propertyList.add("DOCUMENT STATUS");
                        }

                        diff.values().removeIf(Objects::isNull);
                        diff.values()
                            .removeIf(value -> value.equals("null") || value.equals("undefined"));
                        data = mapper.writeValueAsString(diff);
                    }
                    if (propertyList.isEmpty()) {
                        changedProperty = "LOAN DOCUMENT";
                    } else if (propertyList.size() == 1) {
                        changedProperty = propertyList.get(0);
                    } else {
                        String lastIndex = propertyList.get(propertyList.size() - 1);
                        propertyList.remove(propertyList.size() - 1);
                        String list = String.join(", ", propertyList);
                        changedProperty = list + " AND " + lastIndex;
                    }
                    description =
                        c.getId() == null ? String.format(DESCRIPTION_NEW, StringUtil
                            .getStringWithWhiteSpaceAndWithAllFirstLetterCapitalize(
                                loanType.toLowerCase().replace("_", " ")), loanName)
                            : String.format(DESCRIPTION_UPDATE, changedProperty, loanName);
                    break;
                case LOAN_APPROVED:
                    data = mapper.writeValueAsString(c);
                    break;

                default:

            }
            customerActivity = CustomerActivity.builder()
                .modifiedOn(new Date())
                .modifiedBy(userService.getAuthenticatedUser())
                .data(data)
                .profile(c.getLoanHolder())
                .activity(customerLoanLog.value())
                .activityType(ActivityType.MANUAL)
                .customerLoanId(c.getId())
                .description(description)
                .build();
        }
        Object retVal = pjp.proceed();
        try {
            if (ObjectUtils.isEmpty(id)) {
                if (retVal instanceof ResponseEntity) {
                    ResponseEntity<?> restResponseDto = (ResponseEntity<?>) retVal;
                    if (restResponseDto.getStatusCode() == HttpStatus.OK) {
                        RestResponseDto mapResponse = mapper
                            .convertValue(restResponseDto.getBody(), RestResponseDto.class);
                        CustomerLoan customerLoan = mapper
                            .convertValue(mapResponse.getDetail(), CustomerLoan.class);
                        assert customerActivity != null;
                        customerActivity.setCustomerLoanId(customerLoan.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("unable to save customer");
        }
        if (!ObjectUtils.isEmpty(customerActivity.getCustomerLoanId())) {
            this.activityService.saveCustomerActivityByResponseSuccess(customerActivity, retVal);
        }
        return retVal;


    }

    @Data
    static class ProposalData {

        private Long proposedLimit;
        private String premiumRateOnBaseRate;
        private String serviceCharge;
        private String tenureDurationInMonths;
        private String interestRate;
        private String baseRate;
        private String cibCharge;
        private String repaymentMode;
        private String purposeOfSubmission;
        private String disbursementCriteria;
        private String creditInformationReportStatus;
        private String incomeFromTheAccount;
        private String borrowerInformation;

        // Additional Fields--
        // for installment Amount--
        private String installmentAmount;
        // for moratoriumPeriod Amount--
        private String moratoriumPeriod;
        // for prepaymentCharge Amount--
        private String prepaymentCharge;
        // for prepaymentCharge Amount--
        private String purposeOfSubmissionSummary;
        // for commitmentFee Amount--
        private String commitmentFee;
        private String solConclusionRecommendation;
        private String waiverConclusionRecommendation;
        private String riskConclusionRecommendation;
    }
}
