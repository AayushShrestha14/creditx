package com.sb.solutions.api.loan.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupSummaryDto {

    private List<GroupDto> groupDtoList;

    private String groupCode;

    private BigDecimal groupLimit;

    private Long groupId;

    private BigDecimal grandTotalFundedAmount;

    private BigDecimal grandTotalNotFundedAmount;

    private BigDecimal grandTotalPendingLimit;

    private BigDecimal grandTotalApprovedLimit;

}
