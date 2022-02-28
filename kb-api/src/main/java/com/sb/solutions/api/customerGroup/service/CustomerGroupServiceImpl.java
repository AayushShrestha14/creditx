package com.sb.solutions.api.customerGroup.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.customerGroup.repository.CustomerGroupRepository;
import com.sb.solutions.api.customerGroup.repository.specification.CustomerGroupSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;


@Service
public class CustomerGroupServiceImpl extends BaseServiceImpl<CustomerGroup, Long> implements
    CustomerGroupService {

    private final CustomerGroupRepository customerGroupRepository;

    public CustomerGroupServiceImpl(@Autowired CustomerGroupRepository customerGroupRepository) {
        super(customerGroupRepository);

        this.customerGroupRepository = customerGroupRepository;
    }

    @Override
    protected BaseSpecBuilder<CustomerGroup> getSpec(Map<String, String> filterParams) {
        return new CustomerGroupSpecBuilder(filterParams);
    }
}
