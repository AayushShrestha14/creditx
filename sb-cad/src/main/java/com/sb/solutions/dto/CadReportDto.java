package com.sb.solutions.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.entity.CadStage;
import com.sb.solutions.enums.CADDocAction;
import com.sb.solutions.enums.CadDocStatus;

/**
 * @author : Rujan Maharjan on  5/19/2021
 **/

@Data
@NoArgsConstructor
public class CadReportDto {

    private String branchName;
    private String provinceName;
    private String customerName;
    private CustomerType customerType;
    private CadDocStatus docStatus;
    private String cadStageList;
    private String currentPossession;
    private BigDecimal totalProposal;
    private String loanNames;
    private int totalLoan;
    private String fromUser;
    private String lastModifiedAt;
    private CADDocAction docAction;
    private Long totalLifeSpan;


}
