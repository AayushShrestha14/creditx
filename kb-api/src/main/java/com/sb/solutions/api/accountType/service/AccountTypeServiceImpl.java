package com.sb.solutions.api.accountType.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.accountType.entity.AccountType;
import com.sb.solutions.api.accountType.repository.AccountTypeRepository;
import com.sb.solutions.api.accountType.repository.spec.AccountTypeSpecBuilder;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {

    private AccountTypeRepository accountTypeRepository;

    public AccountTypeServiceImpl(
        @Autowired AccountTypeRepository accountTypeRepository
    ) {
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public List<AccountType> findAll() {
        return accountTypeRepository.findAll();
    }

    @Override
    public AccountType findOne(Long id) {
        return accountTypeRepository.getOne(id);
    }

    @Override
    public AccountType save(AccountType accountType) {
        return accountTypeRepository.save(accountType);
    }

    @Override
    public Page<AccountType> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.convertValue(t, Map.class);
        final AccountTypeSpecBuilder builder = new AccountTypeSpecBuilder(map);
        final Specification<AccountType> specification = builder.build();
        return accountTypeRepository.findAll(specification, pageable);
    }

    @Override
    public List<AccountType> saveAll(List<AccountType> list) {
        return accountTypeRepository.saveAll(list);
    }

}
