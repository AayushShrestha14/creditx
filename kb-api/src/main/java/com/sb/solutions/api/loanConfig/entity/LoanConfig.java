package com.sb.solutions.api.loanConfig.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.loanConfig.enums.FinancedAssets;
import com.sb.solutions.api.loanConfig.enums.LoanNature;
import com.sb.solutions.core.enums.LoanTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.loanTemplate.entity.LoanTemplate;
import com.sb.solutions.api.postApprovalDocument.entity.OfferLetter;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanConfig extends BaseEntity<Long> {

    @Column(unique = true)
    private @NotNull(message = "name should not be null") String name;

    @ManyToMany
    private List<LoanTemplate> templateList;

    private Boolean isFundable;

    private String shortNames;

    private LoanNature loanNature;

    private FinancedAssets financedAssets;

    private Double collateralRequirement;

    private Status status;

    private Boolean isRenewable;

    @ManyToMany
    @Where(clause = "status = 1")
    // here the status 1 means Active status
    private List<Document> initial;

    @ManyToMany
    @Where(clause = "status = 1")
    // here the status 1 means Active status
    private List<Document> renew = new ArrayList<>();

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> closure;

    @ManyToMany
    @Where(clause = "status = 1")
    // here the status 1 means Active status
    private List<Document> eligibilityDocuments;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> enhance;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> partialSettlement;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> fullSettlement;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> approvedDocument;

    @ManyToMany
    @Where(clause = "status = 1")
    private List<Document> renewWithEnhancement;

    private Long totalPoints = 0L;

    private Boolean enableEligibility;

    @ManyToMany
    private List<OfferLetter> offerLetters;

    private Double minimumProposedAmount;

    private Double interestRate;

    private LoanApprovalType loanCategory;

    private LoanTag loanTag;
}
