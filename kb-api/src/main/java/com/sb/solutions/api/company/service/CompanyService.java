package com.sb.solutions.api.company.service;

import java.util.Map;

import com.sb.solutions.api.company.entity.Company;
import com.sb.solutions.core.service.BaseService;

public interface CompanyService extends BaseService<Company> {

    Map<Object, Object> companyStatusCount();
}
