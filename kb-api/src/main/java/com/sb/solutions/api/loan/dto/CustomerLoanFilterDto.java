package com.sb.solutions.api.loan.dto;

import static com.sb.solutions.core.constant.AppConstant.DATE_FORMAT;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.entity.CombinedLoan;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.Priority;

/**
 * @author : Rujan Maharjan on  5/6/2021
 **/

@Data
@NoArgsConstructor
public class CustomerLoanFilterDto {

    private Long id;

    private String loanName;
    private Long loanId;

    private LoanType loanType;

    private DocStatus documentStatus;

    private LoanStage currentStage;

    private Priority priority;

    private String previousStageList;

    private CombinedLoan combinedLoan;

    private Proposal proposal;
    private List previousList;

    private LoanConfig loan;
    private Boolean pulled = false;

    private Long loanHolderId;
    private String toUserName;
    private BigDecimal proposedLimit;
    private Date lastModifiedAt;
    public CustomerLoanFilterDto(Long id, String loanName,
        LoanType loanType, DocStatus documentStatus, LoanStage currentStage,
        Priority priority, String previousStageList,
        CombinedLoan combinedLoan,
        Proposal proposal,Long loanId) {
        this.id = id;
        this.loanName = loanName;
        this.loanType = loanType;
        this.documentStatus = documentStatus;
        this.currentStage = currentStage;
        this.priority = priority;
        this.previousStageList = previousStageList;
        this.combinedLoan = combinedLoan;
        this.proposal = proposal;
        this.loanId = loanId;
    }

    public CustomerLoanFilterDto(Long id, String loanName,
        LoanType loanType, DocStatus documentStatus, LoanStage currentStage,
        Priority priority, String previousStageList,
        CombinedLoan combinedLoan,
        Proposal proposal,Long loanId,Long loanHolderId) {
        this.id = id;
        this.loanName = loanName;
        this.loanType = loanType;
        this.documentStatus = documentStatus;
        this.currentStage = currentStage;
        this.priority = priority;
        this.previousStageList = previousStageList;
        this.combinedLoan = combinedLoan;
        this.proposal = proposal;
        this.loanId = loanId;
        this.loanHolderId = loanHolderId;
    }

    public CustomerLoanFilterDto(Long id, String loanName,
        LoanType loanType, DocStatus documentStatus, String toUserName,Date lastModifiedAt,
        Priority priority, String previousStageList,
        CombinedLoan combinedLoan,
        BigDecimal proposedLimit,Long loanId,Long loanHolderId) {
        this.id = id;
        this.loanName = loanName;
        this.loanType = loanType;
        this.documentStatus = documentStatus;
        this.toUserName = toUserName;
        this.lastModifiedAt = lastModifiedAt;
        this.priority = priority;
        this.previousStageList = previousStageList;
        this.combinedLoan = combinedLoan;
        this.proposedLimit = proposedLimit;
        this.loanId = loanId;
        this.loanHolderId = loanHolderId;
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

    public LoanConfig getLoan() {
        LoanConfig loanConfig = new LoanConfig();
        loanConfig.setName(this.loanName);
        loanConfig.setId(this.loanId);
        return loanConfig;
    }
}
