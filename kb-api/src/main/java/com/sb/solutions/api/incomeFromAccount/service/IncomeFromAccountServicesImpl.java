package com.sb.solutions.api.incomeFromAccount.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.incomeFromAccount.entity.IncomeFromAccount;
import com.sb.solutions.api.incomeFromAccount.repository.IncomeFromAccountRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;

@Service
public class IncomeFromAccountServicesImpl extends
    BaseServiceImpl<IncomeFromAccount, Long> implements IncomeFromAccountServices {

    private IncomeFromAccountRepository incomeFromAccountRepository;

    protected IncomeFromAccountServicesImpl(
        IncomeFromAccountRepository repository) {
        super(repository);
    }


    @Override
    protected BaseSpecBuilder<IncomeFromAccount> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
