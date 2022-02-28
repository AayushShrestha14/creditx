package com.sb.solutions.api.loan.dto;

import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.core.enums.DocStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanSummaryDetailDto {
    private String customerName;

    private Long loanHolderId;

    private Long associateId;

    private Proposal proposal;

    private Security security;

    private Long customerLoanId;

    private LoanConfig loanConfig;

    private DocStatus docStatus;
}
