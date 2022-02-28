package com.sb.solutions.api.loan.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loanConfig.enums.FinancedAssets;
import com.sb.solutions.api.loanConfig.enums.LoanNature;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanConfigDto{

    private Long id;

    private String name;


    private Double minimumProposedAmount;

    private String shortNames;

    private LoanNature loanNature;

    private FinancedAssets financedAssets;

    private Double collateralRequirement;

    private long totalPoints;

    private Double interestRate;

    private Boolean isFundable;

    private Boolean isRenewable;
}
