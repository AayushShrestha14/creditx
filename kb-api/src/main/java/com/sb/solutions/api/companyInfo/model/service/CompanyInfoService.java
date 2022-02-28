package com.sb.solutions.api.companyInfo.model.service;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.helper.HelperService;
import com.sb.solutions.core.service.BaseService;

public interface CompanyInfoService extends BaseService<CompanyInfo>,
    HelperService<Long> {

    CompanyInfo findCompanyInfoByRegistrationNumber(String registrationNumber);
    CompanyInfo findCompanyInfoByPanNumber(String panNumber);

}
