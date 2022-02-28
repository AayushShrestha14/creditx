package com.sb.solutions.api.fiscalyear.service;

import com.sb.solutions.api.fiscalyear.entity.FiscalYear;
import com.sb.solutions.core.service.BaseService;

public interface FiscalYearService extends BaseService<FiscalYear> {

    void deActiveFiscalYears();

}
