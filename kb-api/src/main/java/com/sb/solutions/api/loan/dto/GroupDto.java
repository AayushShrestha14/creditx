package com.sb.solutions.api.loan.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GroupDto {

    private BigDecimal totalFunded;
    private BigDecimal totalNonFunded;
    private List<CustomerLoanGroupDto> customerLoanGroupDto;
    private List<CustomerLoanGroupDto> fundedData;
    private List<CustomerLoanGroupDto> nonFundedData;
    private BigDecimal totalApprovedLimit;
    private BigDecimal totalPendingLimit;
}
