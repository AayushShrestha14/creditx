package com.sb.solutions.api.customerActivity.aop;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.companyInfo.model.service.CompanyInfoService;
import com.sb.solutions.api.constant.TemplateNameConstant;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customer.service.CustomerInfoService;
import com.sb.solutions.api.customer.service.CustomerService;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.enums.ActivityType;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.exception.ServiceValidationException;

/**
 * @author : Rujan Maharjan on  9/16/2020
 **/
@Aspect
@Component
@Slf4j
public class CustomerAspect {

    private final ActivityService activityService;

    private final CustomerInfoService customerInfoService;

    private final CustomerService customerService;

    private final CompanyInfoService companyInfoService;

    private final UserService userService;

    private static final String DESCRIPTION_UPDATE = "%s has been updated Successfully";
    private static final String DESCRIPTION_NEW = "New %s has been Saved Successfully";

    private ObjectMapper mapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));

    @Autowired
    public CustomerAspect(ActivityService activityService,
        CustomerInfoService customerInfoService,
        UserService userService,
        CustomerService customerService,
        CompanyInfoService companyInfoService) {
        this.activityService = activityService;
        this.customerInfoService = customerInfoService;
        this.userService = userService;
        this.customerService = customerService;
        this.companyInfoService = companyInfoService;
    }


    @Pointcut("execution(* com.sb.solutions..*.*(..))")
    public void serviceLayer() {
    }

    @AfterThrowing(value = "serviceLayer() && @annotation(customerActivityLog)", throwing = "ex")
    public void doRecoveryActions(CustomerActivityLog customerActivityLog, Exception ex) {
        log.info("check doRecoveryActions customerActionTrack{} and Exception{}",
            customerActivityLog.value(), ex);
    }

    @Around(value = "serviceLayer() && @annotation(customerActionTrack) && args(batch,id, template,..)")
    public Object trackAroundCustomerInfoProperty(ProceedingJoinPoint pjp, Object batch,
        CustomerActivityLog customerActionTrack, Long id, String template)
        throws Throwable {
        log.info(" previous customer info property on basis of customerInfoId {} template {}", id,
            template);
        Optional<CustomerInfo> prevData = customerInfoService.findOne(id);
        CustomerInfo customerInfoPrev;
        if (prevData.isPresent()) {
            customerInfoPrev = prevData.get();
        } else {
            throw new ServiceValidationException("No Customer Found");
        }
        CustomerInfo profile = customerInfoPrev;
        String previousData = getPreviousDataByTemplateSelected(template, customerInfoPrev);
        String description = String.format(DESCRIPTION_UPDATE, template);
        if (ObjectUtils.isEmpty(previousData) || previousData.equalsIgnoreCase("null")) {
            description = String.format(DESCRIPTION_NEW, template);
        }

        CustomerActivity customerActivity = CustomerActivity.builder()
            .profile(profile)
            .data(previousData)
            .activity(activity(template))
            .description(description)
            .modifiedBy(getCurrentUser())
            .modifiedOn(new Date())
            .activityType(getCurrentUser() == null ? ActivityType.SCHEDULE : ActivityType.MANUAL)
            .build();

        Object retVal = pjp.proceed();
        this.activityService.saveCustomerActivityByResponseSuccess(customerActivity, retVal);
        return retVal;


    }


    private String getPreviousDataByTemplateSelected(String template, CustomerInfo customerInfo)
        throws JsonProcessingException {
        switch (template) {
            case TemplateNameConstant.INSURANCE:
                return mapper.writeValueAsString(customerInfo.getInsurance());

            case TemplateNameConstant.SITE_VISIT:
                return mapper.writeValueAsString(customerInfo.getSiteVisit());

            case TemplateNameConstant.GUARANTOR:
                return mapper.writeValueAsString(customerInfo.getGuarantors());

            case TemplateNameConstant.FINANCIAL:
                return mapper.writeValueAsString(customerInfo.getFinancial());

            case TemplateNameConstant.SECURITY:
                return mapper.writeValueAsString(customerInfo.getSecurity());

            case TemplateNameConstant.SHARE_SECURITY:
                return mapper.writeValueAsString(customerInfo.getShareSecurity());

            case TemplateNameConstant.CUSTOMER_GROUP:
                return mapper.writeValueAsString(customerInfo.getCustomerGroup());

            case TemplateNameConstant.CRG:
                return mapper.writeValueAsString(customerInfo.getCreditRiskGrading());

            case TemplateNameConstant.CRG_ALPHA:
                return mapper.writeValueAsString(customerInfo.getCreditRiskGradingAlpha());

            case TemplateNameConstant.CRG_GAMMA:
                return mapper.writeValueAsString(customerInfo.getCrgGamma());

            case TemplateNameConstant.CICL:
                return mapper.writeValueAsString(customerInfo.getCicl());
            default:
                return null;
        }
    }

    private Activity activity(String template) {
        switch (template) {
            case TemplateNameConstant.INSURANCE:
                return Activity.INSURANCE_UPDATE;

            case TemplateNameConstant.SITE_VISIT:
                return Activity.SITE_VISIT_UPDATE;

            case TemplateNameConstant.GUARANTOR:
                return Activity.GUARANTOR_UPDATE;

            case TemplateNameConstant.FINANCIAL:
                return Activity.FINANCIAL_UPDATE;

            case TemplateNameConstant.SECURITY:
                return Activity.SECURITY;

            case TemplateNameConstant.SHARE_SECURITY:
                return Activity.SHARE_SECURITY;

            case TemplateNameConstant.CUSTOMER_GROUP:
                return Activity.CUSTOMER_GROUP_UPDATE;

            case TemplateNameConstant.CRG:
                return Activity.CRG_UPDATE;

            case TemplateNameConstant.CRG_ALPHA:
                return Activity.CRG_ALPHA_UPDATE;

            case TemplateNameConstant.CRG_GAMMA:
                return Activity.CRG_GAMMA_UPDATE;

            case TemplateNameConstant.CICL:
                return Activity.CICL_UPDATE;
            default:
                return Activity.CUSTOMER_UPDATE;
        }
    }

    private User getCurrentUser() {
        return userService.getAuthenticatedUser();
    }

    @Around(value = "serviceLayer() && @annotation(customerActionTrack) && args(batch,..)")
    public Object trackAroundCustomer(ProceedingJoinPoint pjp, Object batch,
        CustomerActivityLog customerActionTrack)
        throws Throwable {
        final CustomerActivity activity = mapObjectToEntityCustomerOrCompany(batch);
        Object retVal = pjp.proceed();
        if (!ObjectUtils.isEmpty(activity.getProfile())) {
            this.activityService.saveCustomerActivityByResponseSuccess(activity, retVal);
        }
        return retVal;


    }

    private CustomerActivity mapObjectToEntityCustomerOrCompany(Object o)
        throws JsonProcessingException {
        CustomerActivity customerActivity = new CustomerActivity();
        customerActivity.setModifiedBy(getCurrentUser());
        customerActivity.setModifiedOn(new Date());
        customerActivity.setActivity(Activity.CUSTOMER_UPDATE);
        customerActivity.setActivityType(ActivityType.MANUAL);
        customerActivity.setDescription(String.format(DESCRIPTION_UPDATE, "Customer"));
        String data = null;
        if (o instanceof Customer) {
            Customer c = (Customer) o;
            if (!ObjectUtils.isEmpty(c.getId())) {
                CustomerInfo customerInfo = customerInfoService
                    .findByAssociateIdAndCustomerType(c.getId(), CustomerType.INDIVIDUAL);
                c = customerService.findOne(c.getId());
                data = mapper.writeValueAsString(c);
                customerActivity.setProfile(customerInfo);
                customerActivity.setData(data);

            }
        }

        if (o instanceof CompanyInfo) {
            CompanyInfo c = (CompanyInfo) o;
            if (!ObjectUtils.isEmpty(c.getId())) {
                CustomerInfo customerInfo = customerInfoService
                    .findByAssociateIdAndCustomerType(c.getId(), CustomerType.INSTITUTION);
                c = companyInfoService.findOne(c.getId());
                data = mapper.writeValueAsString(c);
                customerActivity.setProfile(customerInfo);
                customerActivity.setData(data);

            }
        }

        return customerActivity;

    }


}
