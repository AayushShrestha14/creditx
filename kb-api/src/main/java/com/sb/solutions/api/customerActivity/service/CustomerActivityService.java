package com.sb.solutions.api.customerActivity.service;

import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.core.service.Service;

import java.util.List;

/**
 * @author : Rujan Maharjan on  9/18/2020
 **/
public interface CustomerActivityService extends Service<CustomerActivity, Long> {

    List<CustomerActivity> findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(Activity activity, Long id);

}
