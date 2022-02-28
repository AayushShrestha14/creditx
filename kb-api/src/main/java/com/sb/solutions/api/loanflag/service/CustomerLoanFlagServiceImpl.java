package com.sb.solutions.api.loanflag.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.api.loanflag.repository.CustomerLoanFlagRepository;
import com.sb.solutions.api.loanflag.repository.spec.CustomerLoanFlagSpecBuilder;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

/**
 * @author Elvin Shrestha on 4/28/2020
 */
@Service
public class CustomerLoanFlagServiceImpl extends BaseServiceImpl<CustomerLoanFlag, Long> implements
    CustomerLoanFlagService {

    private final CustomerLoanFlagRepository repository;

    public CustomerLoanFlagServiceImpl(
        CustomerLoanFlagRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    protected BaseSpecBuilder<CustomerLoanFlag> getSpec(Map<String, String> filterParams) {
        return new CustomerLoanFlagSpecBuilder(filterParams);
    }

    @Override
    public void deleteCustomerLoanFlagById(Long id) {
        repository.deleteCustomerLoanFlagById(id);
    }

    @Override
    public List<CustomerLoanFlag> findAllByCustomerInfoId(Long id) {
        return repository.findAllByCustomerInfoId(id);
    }

    @Override
    public void updateEmailStatus(boolean flag, Long flagId) {
        repository.updateEmailStatus(flag, flagId);
    }
}
