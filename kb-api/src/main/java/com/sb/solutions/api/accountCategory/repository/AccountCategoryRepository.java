package com.sb.solutions.api.accountCategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;
import com.sb.solutions.api.accountType.entity.AccountType;

@Transactional
public interface AccountCategoryRepository extends JpaRepository<AccountCategory, Long>,
    JpaSpecificationExecutor<AccountCategory> {

    List<AccountCategory> findAllByAccountType(AccountType type);
}
