package com.sb.solutions.api.customerActivity.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.sb.solutions.api.customerActivity.aop.Activity;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.repository.CustomerActivityRepository;
import com.sb.solutions.api.customerActivity.repository.specification.CustomerActivitySpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author : Rujan Maharjan on  9/18/2020
 **/
@Service
public class CustomerActivityServiceImpl extends BaseServiceImpl<CustomerActivity, Long> implements
    CustomerActivityService {

    private final CustomerActivityRepository activityRepository;

    protected CustomerActivityServiceImpl(
        CustomerActivityRepository activityRepository) {
        super(activityRepository);
        this.activityRepository = activityRepository;
    }

    @Override
    public Optional<CustomerActivity> findOne(Long id) {
        Optional<CustomerActivity> customerActivity = activityRepository.findById(id);
        if (customerActivity.isPresent()) {
            return customerActivity;
        }
        return Optional.empty();
    }

    @Override
    public CustomerActivity save(CustomerActivity customerActivity) {
        return activityRepository.save(customerActivity);
    }


    @Override
    public List<CustomerActivity> saveAll(List<CustomerActivity> list) {
        return null;
    }

    @Override
    protected BaseSpecBuilder<CustomerActivity> getSpec(Map<String, String> filterParams) {
        filterParams.values().removeIf(Objects::isNull);
        filterParams.values().removeIf(value -> value.equals("null") || value.equals("undefined"));
        return new CustomerActivitySpecBuilder(filterParams);
    }

    @Override
    public List<CustomerActivity> findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(Activity activity, Long id) {
        return activityRepository.findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(activity,id);
    }
}
