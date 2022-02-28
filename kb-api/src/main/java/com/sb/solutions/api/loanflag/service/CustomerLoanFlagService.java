package com.sb.solutions.api.loanflag.service;

import java.util.List;

import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.core.service.Service;

/**
 * @author Elvin Shrestha on 4/28/2020
 */
public interface CustomerLoanFlagService extends Service<CustomerLoanFlag, Long> {

    void deleteCustomerLoanFlagById(Long id);

    List<CustomerLoanFlag> findAllByCustomerInfoId(Long id);

    void updateEmailStatus(boolean flag, Long flagId);
}
