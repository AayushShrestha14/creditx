package com.sb.solutions.api.loan.dao;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.loan.dto.CustomerApprovedLoanDto;

/**
 * @author : Rujan Maharjan on  3/8/2021
 **/
public interface CustomerApprovedLoanDao {

    List<CustomerApprovedLoanDto> getCADLoanInCurrentUser(Long id,List<Long> ids);

}
