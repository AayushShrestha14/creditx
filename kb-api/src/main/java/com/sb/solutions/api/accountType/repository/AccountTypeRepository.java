package com.sb.solutions.api.accountType.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.sb.solutions.api.accountType.entity.AccountType;

@Transactional
public interface AccountTypeRepository extends JpaRepository<AccountType, Long>,
    JpaSpecificationExecutor<AccountType> {

}
