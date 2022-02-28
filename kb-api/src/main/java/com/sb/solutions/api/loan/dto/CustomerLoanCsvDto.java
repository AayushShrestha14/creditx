package com.sb.solutions.api.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;


/**
 * @author yunish on 11/12/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerLoanCsvDto {

    private Customer customerInfo;

    private LoanConfig loan;
    private String loanIdCode;

    private CompanyInfo companyInfo;

    private CustomerInfo loanHolder;

    private LoanType loanType = LoanType.NEW_LOAN;

    private DocStatus documentStatus = DocStatus.PENDING;

    private LoanApprovalType loanCategory;

    private LoanStage currentStage;

    private Branch branch;
    private Branch Province;

    private User toUser;
    private User FromUser;
    private String stageUsers;

    private Role toRole;

    private Proposal proposal;

    private long loanSpan;

    private long loanPendingSpan;

    private long loanPossession;

    private String createdAt;

    //this is a placeholder for combining two or more props
    private String data;

}
