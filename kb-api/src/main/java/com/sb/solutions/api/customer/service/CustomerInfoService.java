package com.sb.solutions.api.customer.service;

import java.util.Date;
import java.util.List;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerIdType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.loan.dto.CustomerListDto;
import com.sb.solutions.core.service.Service;
import com.sb.solutions.report.core.service.FormReportGeneratorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
public interface CustomerInfoService extends Service<CustomerInfo, Long>,
    FormReportGeneratorService {

    @Transactional
    CustomerInfo saveObject(Object o);

    String csv(Object searchDto);

    CustomerInfo saveLoanInformation(Object o, Long customerInfoId, String template);

    CustomerInfo findByAssociateIdAndCustomerType(Long id, CustomerType customerType);

    CustomerInfo findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(
        CustomerType customerType, String idNumber, String idRegPlace,
        CustomerIdType customerIdType, Date date);

    String updateObligor(String obligor,Long id);

    Object updateNepaliConfigData(String nepData, Long id);

    CustomerInfo updateCustomerBranch(Long customerInfoId, Long branchId);

    Page<CustomerListDto> getCustomerListDto(Object search, Pageable pageable);
}
