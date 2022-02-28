package com.sb.solutions.api.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.core.enums.LoanType;
import lombok.Data;

import com.sb.solutions.core.enums.DocStatus;

import java.util.Date;

/**
 * @author : Rujan Maharjan on  9/28/2020
 **/
@Data
public class CustomerLoanDto {

    private String loanHolderId;
    private String name;
    private String groupCode;
    private String groupLimit;
    private String groupId;
    private String proposedLimit;
    private DocStatus documentStatus;
    private String customerLoanId;
    private Proposal proposal;
    private LoanType loanType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt = new Date();
    private LoanConfig loanConfig;
}
