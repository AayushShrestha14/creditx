package com.sb.solutions.api.customer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerIdType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customer.repository.CustomerRepository;
import com.sb.solutions.api.customer.repository.specification.CustomerSpecBuilder;
import com.sb.solutions.core.exception.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerInfoService customerInfoService;

    public CustomerServiceImpl(
            @Autowired CustomerRepository customerRepository,
            CustomerInfoService customerInfoService) {
        this.customerRepository = customerRepository;
        this.customerInfoService = customerInfoService;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findOne(Long id) {
        return customerRepository.getOne(id);
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        if (!customer.isValid()) {
            throw new ServiceValidationException(
                    customer.getValidationMsg());
        }
        if (ObjectUtils.isEmpty(customer.getId())) {
            CustomerInfo isExist = customerInfoService
                    .findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(
                            CustomerType.INDIVIDUAL, customer.getCitizenshipNumber(),
                            customer.getCitizenshipIssuedPlace(),
                            CustomerIdType.CITIZENSHIP, customer.getCitizenshipIssuedDate());
            if (!ObjectUtils.isEmpty(isExist)) {
                Branch branch = isExist.getBranch();
                throw new ServiceValidationException(
                        "Customer Exist!" + "This Customer is associate with branch " + branch
                                .getName());
            }
        }
        final Customer customer1 = customerRepository.save(customer);
        if (!ObjectUtils.isEmpty(customer1)) {
            customerInfoService.saveObject(customer);
        }

        return customer1;
    }

    @Override
    public Page<Customer> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        s.values().removeIf(Objects::isNull);
        final CustomerSpecBuilder customerSpecBuilder = new CustomerSpecBuilder(s);

        Specification<Customer> specification = customerSpecBuilder.build();
        return customerRepository.findAll(specification, pageable);
    }

    @Override
    public List<Customer> saveAll(List<Customer> list) {
        return customerRepository.saveAll(list);
    }

    @Override
    public List<Customer> findCustomerByCitizenshipNumber(String citizenshipNumber) {
        return customerRepository.findCustomerByCitizenshipNumber(citizenshipNumber);
    }

    @Override
    public Customer findCustomerByCustomerNameAndCitizenshipNumberAndCitizenshipIssuedDate(
            String name, String citizenship, Date citizenIssueDate) {
        Customer customer = customerRepository
                .findCustomerByCustomerNameAndCitizenshipNumberAndCitizenshipIssuedDate(name,
                        citizenship, citizenIssueDate);
        if (customer == null) {
            throw new ServiceValidationException("No customer Found");
        }
        return customer;
    }
}

