package com.sb.solutions.api.loan.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.core.enums.DocStatus;

@NoArgsConstructor

@Data
public class CustomerLoanGroupDto {

    private String customerName;

    private Long loanHolderId;

    private Long associateId;

    private Proposal proposal;

    private Security security;

    private Long customerLoanId;

    private LoanConfig loanConfig;

    private DocStatus docStatus;

    public CustomerLoanGroupDto(Long customerLoanId, String customerName , Long loanHolderId , Long associateId , Proposal proposal,
        Security security , DocStatus docStatus , LoanConfig loanConfig) {
        this.customerLoanId = customerLoanId;
        this.loanHolderId = loanHolderId;
        this.associateId = associateId;
        this.customerName = customerName;
        this.proposal = proposal;
        this.security = security;
        this.docStatus = docStatus;
        this.loanConfig = loanConfig;
    }


}
