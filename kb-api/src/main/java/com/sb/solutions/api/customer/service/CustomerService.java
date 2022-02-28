package com.sb.solutions.api.customer.service;

import java.util.Date;
import java.util.List;

import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.core.service.BaseService;

public interface CustomerService extends BaseService<Customer> {

    List<Customer> findCustomerByCitizenshipNumber(String citizenshipNumber);

    Customer findCustomerByCustomerNameAndCitizenshipNumberAndCitizenshipIssuedDate(String name,
        String citizenship,
        Date citizenIssueDate);

}
