package com.sb.solutions.admin.loanconfiguration;

import com.sb.solutions.api.loanConfig.enums.FinancedAssets;
import com.sb.solutions.api.loanConfig.enums.LoanNature;
import lombok.Data;

import java.util.List;

@Data
public class LoanConfigAdminDto {

    private Long id;

    private String name;

    private List<AdminDocumentDto> eligibilityDocuments;

    private Double minimumProposedAmount;

    private String shortNames;

    private LoanNature loanNature;

    private FinancedAssets financedAssets;

    private Double collateralRequirement;

    private long totalPoints;

    private Double interestRate;
}
