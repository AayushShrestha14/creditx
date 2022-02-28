package com.sb.solutions.api.customerOtp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.customerOtp.entity.CustomerOtp;
import com.sb.solutions.api.customerOtp.repository.CustomerOtpRepository;

@Service
public class CustomerOtpServiceImpl implements CustomerOtpService {

    private final CustomerOtpRepository repository;

    public CustomerOtpServiceImpl(
        CustomerOtpRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public List<CustomerOtp> findAll() {
        return repository.findAll();
    }

    @Override
    public CustomerOtp findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public CustomerOtp save(CustomerOtp customerOtp) {
        return repository.save(customerOtp);
    }

    @Override
    public Page<CustomerOtp> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CustomerOtp> saveAll(List<CustomerOtp> list) {
        return repository.saveAll(list);
    }

    @Override
    public void delete(CustomerOtp customerOtp) {
        repository.delete(customerOtp);
    }

    @Override
    public CustomerOtp findByEmailOrMobile(String email, String mobile) {
        return repository.findCustomerOtpByEmailOrMobile(email, mobile);
    }
}
