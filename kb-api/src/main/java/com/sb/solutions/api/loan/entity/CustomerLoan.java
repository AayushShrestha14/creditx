package com.sb.solutions.api.loan.entity;

import static com.sb.solutions.core.constant.AppConstant.DATE_FORMAT;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.sb.solutions.api.collateralSiteVisit.dto.CollateralSiteVisitDto;
import com.sb.solutions.api.crgMicro.entity.CrgMicro;
import com.sb.solutions.api.loan.dto.CustomerLoanDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.creditRiskGrading.entity.CreditRiskGrading;
import com.sb.solutions.api.creditRiskGradingAlpha.entity.CreditRiskGradingAlpha;
import com.sb.solutions.api.creditRiskGradingLambda.entity.CreditRiskGradingLambda;
import com.sb.solutions.api.crg.entity.CrgGamma;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;
import com.sb.solutions.api.financial.entity.Financial;
import com.sb.solutions.api.group.entity.Group;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.guarantor.entity.GuarantorDetail;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.dto.CustomerOfferLetterDto;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loan.dto.NepaliTemplateDto;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.reportinginfo.entity.ReportingInfoLevel;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.api.sharesecurity.ShareSecurity;
import com.sb.solutions.api.siteVisit.entity.SiteVisit;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.vehiclesecurity.entity.VehicleSecurity;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.PostApprovalAssignStatus;
import com.sb.solutions.core.enums.Priority;

/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
@Audited
public class CustomerLoan extends BaseEntity<Long> {

    @NotAudited
    @ManyToOne
    private Customer customerInfo;


    @NotAudited
    @ManyToOne
    private CustomerInfo loanHolder;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne
    private LoanConfig loan;

    @Audited
    @ManyToOne
    private CompanyInfo companyInfo;

    private LoanType loanType = LoanType.NEW_LOAN;

    private DocStatus documentStatus = DocStatus.DISCUSSION;

    private LoanApprovalType loanCategory;

    @AuditJoinTable(name = "customer_document_path_customer_loan_audit")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "customer_document_path_customer_loan")
    private List<CustomerDocument> customerDocument = new ArrayList<>();

    @NotAudited
    @ManyToMany
    private List<CadDocument> cadDocument;

    @NotAudited
    @Transient
    @ManyToOne
    private DmsLoanFile dmsLoanFile;

    @Audited
    @Transient
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_visit_id")
    private SiteVisit siteVisit;

    @NotAudited
    @Transient
    @OneToOne
    private ShareSecurity shareSecurity;

    @Audited
    @OneToOne(cascade = CascadeType.ALL)
    private LoanStage currentStage;

    @NotAudited
    private Priority priority;

    @NotAudited
    private Long parentId;

    @NotAudited
    private Long childId;

    @NotAudited
    private Boolean isCloseRenew;

    @Transient
    private List previousList;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @OneToOne
    private Branch branch;

    @NotAudited
    private Boolean notify;

    @NotAudited
    private Long notedBy;

    private String offerLetterUrl;

    @Audited
    @OneToOne
    private Proposal proposal;

    @Audited
    @Transient
    @OneToOne
    private Financial financial;

    @Audited
    @Transient
    @OneToOne
    private Security security;

    @NotAudited
    @Transient
    @OneToOne
    private Group group;

    @NotAudited
    @Transient
    @OneToOne
    private VehicleSecurity vehicleSecurity;

    @NotAudited
    @OneToOne
    private CreditRiskGradingAlpha creditRiskGradingAlpha;

    @NotAudited
    @Transient
    @OneToOne(cascade = CascadeType.ALL)
    private GuarantorDetail guarantor;

    @NotAudited
    @ManyToMany
    @JoinTable(name = "customer_loan_guarantor",
        joinColumns = @JoinColumn(name = "customer_loan_id"),
        inverseJoinColumns = @JoinColumn(name = "guarantor_id"))
    private Set<Guarantor> taggedGuarantors;

    @Lob
    private String previousStageList;

    @NotAudited
    private Boolean isValidated = false;

    @Transient
    private Boolean pulled = false;

    @Transient
    private CustomerOfferLetterDto customerOfferLetter;

    @Transient
    private List<LoanStageDto> distinctPreviousList;

    @Transient
    private int offerLetterStat = 0;

    @Transient
    private int uploadedOfferLetterStat = 0;

    @Audited
    @Transient
    @OneToOne
    private CreditRiskGrading creditRiskGrading;

    @NotAudited
    @OneToOne
    private CreditRiskGradingLambda creditRiskGradingLambda;

    @NotAudited
    @OneToOne
    private CrgMicro crgMicro;

    @NotAudited
    @OneToOne
    private CrgGamma crgGamma;

    @Transient
    private List<NepaliTemplateDto> nepaliTemplates = new ArrayList<>();

    @NotAudited
    @ManyToMany
    private List<ReportingInfoLevel> reportingInfoLevels;

    @NotAudited
    @Transient
    @OneToMany
    private List<Insurance> insurance;

    @NotAudited
    private String refNo;

    @NotAudited
    @ManyToOne
    private CombinedLoan combinedLoan;

    @Transient
    private Object reportingInfoLog;

    @Transient
    private Object groupSummaryDto;

    @NotAudited
    private PostApprovalAssignStatus postApprovalAssignStatus = PostApprovalAssignStatus.NOT_ASSIGNED;

    @NotAudited
    @OneToOne
    private User postApprovalAssignedUser;

    @NotAudited
    private String cbsLoanFileNumber;

    @NotAudited
    private Boolean isSol = Boolean.FALSE;

    @NotAudited
    @OneToOne
    private User solUser;

    private String postApprovalDocIdList;

    private String authorityReviewComments;
    @NotAudited
    private String data;

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public List<LoanStageDto> getPreviousList() {
        if (this.getPreviousStageList() != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            try {
                this.previousList = objectMapper.readValue(this.getPreviousStageList(),
                    typeFactory.constructCollectionType(List.class, LoanStageDto.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.previousList = new ArrayList();
        }
        return this.previousList;
    }

    public List<LoanStageDto> getDistinctPreviousList() {
        Collection<LoanStageDto> list =
            CollectionUtils.isEmpty(this.getPreviousList()) || CollectionUtils
                .isEmpty(this.previousList) ? new ArrayList<>() : this.getPreviousList();
        return list.stream()
            .filter(distinctByKey(
                p -> (p.getToUser() == null ? p.getToRole().getId() : p.getToUser().getId())))
            .filter(p -> !p.getToUser().getIsDefaultCommittee())
            .collect(Collectors.toList());
    }

    public LoanApprovalType getLoanCategory() {
        return this.loanApprovalTypeByLoanHolder();
    }

    private LoanApprovalType loanApprovalTypeByLoanHolder() {
        CustomerType customerType = this.loanHolder.getCustomerType();
        if (customerType.equals(CustomerType.INDIVIDUAL)) {
            return LoanApprovalType.INDIVIDUAL;
        }
        return LoanApprovalType.INSTITUTION;
    }

    @Transient
    private String zipPath;

    @Transient
    private List<CustomerLoanDto> customerLoanDtoList;

    @NotAudited
    @Transient
    private List<CollateralSiteVisitDto> collateralSiteVisits;
}
