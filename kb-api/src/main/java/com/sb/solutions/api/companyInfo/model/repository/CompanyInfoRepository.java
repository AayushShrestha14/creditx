package com.sb.solutions.api.companyInfo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Long>,
    JpaSpecificationExecutor<CompanyInfo> {

    CompanyInfo findCompanyInfoByRegistrationNumber(String registrationNumber);

    CompanyInfo findCompanyInfoByPanNumber(String panNumber);

}
