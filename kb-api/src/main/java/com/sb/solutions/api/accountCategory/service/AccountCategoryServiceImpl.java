package com.sb.solutions.api.accountCategory.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;
import com.sb.solutions.api.accountCategory.repository.AccountCategoryRepository;
import com.sb.solutions.api.accountCategory.repository.spec.AccountCategorySpecBuilder;
import com.sb.solutions.api.accountType.entity.AccountType;

@Service
public class AccountCategoryServiceImpl implements AccountCategoryService {

    private final AccountCategoryRepository accountCategoryRepository;

    public AccountCategoryServiceImpl(
        @Autowired AccountCategoryRepository accountCategoryRepository
    ) {
        this.accountCategoryRepository = accountCategoryRepository;
    }

    @Override
    public List<AccountCategory> findAll() {
        return accountCategoryRepository.findAll();
    }

    @Override
    public AccountCategory findOne(Long id) {
        return accountCategoryRepository.getOne(id);
    }

    @Override
    public AccountCategory save(AccountCategory accountCategory) {
        return accountCategoryRepository.save(accountCategory);
    }

    @Override
    public Page<AccountCategory> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.convertValue(t, Map.class);
        final AccountCategorySpecBuilder builder = new AccountCategorySpecBuilder(map);
        final Specification<AccountCategory> specification = builder.build();
        return accountCategoryRepository.findAll(specification, pageable);
    }

    @Override
    public List<AccountCategory> saveAll(List<AccountCategory> list) {
        return accountCategoryRepository.saveAll(list);
    }

    @Override
    public List<AccountCategory> findAllByAccountTypeId(Long id) {
        AccountType accountType = new AccountType();
        accountType.setId(id);
        return accountCategoryRepository.findAllByAccountType(accountType);
    }
}
