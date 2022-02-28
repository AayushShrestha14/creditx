package com.sb.solutions.web.eligibility.v1.loanconfig.dto;

import java.util.List;

import com.sb.solutions.api.loanConfig.enums.FinancedAssets;
import com.sb.solutions.api.loanConfig.enums.LoanNature;
import lombok.Data;

@Data
public class LoanConfigDto {

    private Long id;

    private String name;

    private List<DocumentDto> eligibilityDocuments;

    private Double minimumProposedAmount;

    private String shortNames;

    private LoanNature loanNature;

    private FinancedAssets financedAssets;

    private Double collateralRequirement;

    private long totalPoints;

    private Double interestRate;
}
