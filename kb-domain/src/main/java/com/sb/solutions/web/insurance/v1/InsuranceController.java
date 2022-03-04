package com.sb.solutions.web.insurance.v1;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.enums.ActivityType;
import com.sb.solutions.api.customerActivity.service.CustomerActivityService;
import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperIdType;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.insurance.service.InsuranceService;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;

/**
 * @author Elvin Shrestha on 4/19/2020
 */
@RestController
@RequestMapping(InsuranceController.URL)
@Slf4j
public class InsuranceController {

    static final String URL = "/v1/insurance";
    private static final String DESCRIPTION_UPDATE = "%s has been updated Successfully";
    private final InsuranceService insuranceService;
    private final CustomerLoanService customerLoanService;
    private final CustomerActivityService customerActivityLogService;
    private final UserService userService;

    public InsuranceController(
        InsuranceService insuranceService,
        CustomerLoanService customerLoanService,
        CustomerActivityService customerActivityLogService,
        UserService userService) {
        this.insuranceService = insuranceService;
        this.customerLoanService = customerLoanService;
        this.customerActivityLogService = customerActivityLogService;
        this.userService = userService;
    }

    @PostMapping("/history")
    public ResponseEntity<?> save(@RequestParam Long loanId,
        @RequestBody List<Insurance> insurances) {
        CustomerLoan customerLoan = customerLoanService.findOne(loanId);

        if (customerLoan == null) {
            return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "Loan not found");
        }

        CustomerActivity activityLog = new CustomerActivity();
        activityLog.setProfile(customerLoan.getLoanHolder());
        activityLog.setActivity(Activity.INSURANCE_UPDATE);
        activityLog.setActivityType(ActivityType.MANUAL);
        activityLog.setDescription(String.format(DESCRIPTION_UPDATE, "Insurance"));
        activityLog.setModifiedOn(new Date());
        activityLog.setModifiedBy(userService.getAuthenticatedUser());
        List<Insurance> existingInsurances = insurances.stream()
            .map(i -> insuranceService.findOne(i.getId()).get())
            .collect(Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            activityLog.setData(objectMapper.writeValueAsString(existingInsurances));
        } catch (JsonProcessingException e) {
            log.error("Error saving insurance history {}", e.getMessage());
            return new RestResponseDto()
                .failureModel(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving insurance history");
        }
        customerActivityLogService.save(activityLog);

        insuranceService.saveAll(insurances);

        // flag checks
        HelperDto<Long> dto = new HelperDto<>(customerLoan.getLoanHolder().getId(),
            HelperIdType.CUSTOMER_INFO);
        insuranceService.execute(Optional.of(dto));
        return new RestResponseDto().successModel("Insurance detail updated");
    }
}
