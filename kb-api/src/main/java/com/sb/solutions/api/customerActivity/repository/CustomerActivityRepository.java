package com.sb.solutions.api.customerActivity.repository;

import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.core.repository.BaseRepository;

import java.util.List;

/**
 * @author : Rujan Maharjan on  9/18/2020
 **/
public interface CustomerActivityRepository extends BaseRepository<CustomerActivity, Long> {

    List<CustomerActivity> findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(Activity activity,Long id);
}
