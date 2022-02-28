package com.sb.solutions.api.accountCategory.service;

import java.util.List;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;
import com.sb.solutions.core.service.BaseService;

public interface AccountCategoryService extends BaseService<AccountCategory> {

    List<AccountCategory> findAllByAccountTypeId(Long id);
}
