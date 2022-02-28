package com.sb.solutions.api.customerActivity.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.service.CustomerActivityService;

import java.util.List;

/**
 * @author : Rujan Maharjan on  9/22/2020
 **/

@Service
@Slf4j
public class ActivityService {

    private final CustomerActivityService customerActivityService;

    public ActivityService(
            CustomerActivityService customerActivityService) {
        this.customerActivityService = customerActivityService;
    }

    public void saveCustomerActivity(CustomerActivity customerActivity) {
        new Thread(() -> {
            try {
                this.customerActivityService.save(customerActivity);
            } catch (Exception e) {
                log.error("error saving customer activity", e);
            }
        }).start();
    }

    public void saveCustomerActivityByResponseSuccess(CustomerActivity customerActivity,
                                                      Object retVal) {
        ResponseEntity<?> restResponseDto;
        if (retVal instanceof ResponseEntity) {
            restResponseDto = (ResponseEntity<?>) retVal;
            if (restResponseDto.getStatusCode() == HttpStatus.OK) {
                saveCustomerActivity(customerActivity);
                log.info("saving customer Activity {} of customer {} and id {} with associateId {}",
                        customerActivity.getActivity(),
                        customerActivity.getProfile().getName(), customerActivity.getProfile().getId(),
                        customerActivity.getProfile().getAssociateId());
            }
        }
    }

    public List<CustomerActivity> findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(Activity activity, Long id) {
      return  customerActivityService.findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(activity, id);
    }


}
