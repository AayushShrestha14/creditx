package com.sb.solutions.api.preference.interestrate.service;

import com.sb.solutions.api.preference.interestrate.entity.BaseInterestRate;
import com.sb.solutions.core.service.BaseService;

public interface BaseInterestService extends BaseService<BaseInterestRate> {
    public BaseInterestRate getActiveRate();
}
