package com.sb.solutions.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.utils.FilterJsonUtils;

/**
 * @author : Rujan Maharjan on  8/8/2021
 **/
@Data
public class CadAssignedLoanDto {

    private Proposal proposal;

    private List<LoanStageDto> distinctPreviousList;

    private List previousList;

    private DocStatus documentStatus;

    private LoanStage currentStage;

    private LoanConfig loan;

    private LoanType loanType;

    private Long cadId;

    private Long customerLoanId;

    private String loanName;

    private Long loanId;

    private List<CustomerLoan> assigned;

    private String previousStageList;

    public CadAssignedLoanDto(
        LoanStage currentStage, LoanType loanType, Long cadId, Long customerLoanId,
        Proposal proposal, String loanName, Long loanId, String previousStageList) {
        this.currentStage = currentStage;
        this.loanType = loanType;
        this.cadId = cadId;
        this.customerLoanId = customerLoanId;
        this.proposal = proposal;
        this.loanName = loanName;
        this.loanId = loanId;
        this.previousStageList = previousStageList;
    }

    public LoanConfig getLoan() {
        LoanConfig loanConfig = new LoanConfig();
        loanConfig.setId(this.loanId);
        loanConfig.setName(this.loanName);
        return loanConfig;
    }

    public LoanStage getCurrentStage() {
        if (!ObjectUtils.isEmpty(this.currentStage)) {
            if (this.currentStage.getDocAction().equals(DocAction.APPROVED)) {
                return this.currentStage;
            } else {
                List<LoanStage> list = FilterJsonUtils
                    .stringToJSon(this.previousStageList, LoanStage.class);
                Optional<LoanStage> stage =  list.stream()
                    .filter(loanStage -> loanStage.getDocAction().equals(DocAction.APPROVED)).findFirst();
                if(stage.isPresent()){
                    return stage.get();
                }
            }
        }
        return this.currentStage;

    }

    public List<LoanStage> getPreviousList() {
        return FilterJsonUtils.stringToJSon(this.previousStageList, LoanStage.class);
    }

    public static List<CustomerLoan> getAssigned(List<CadAssignedLoanDto> cadAssignedLoanDto,
        CustomerInfo customerInfo) {
        List<CustomerLoan> customerLoanList = new ArrayList<>();
        cadAssignedLoanDto.forEach(c -> {
            CustomerLoan customerLoan = new CustomerLoan();
            customerLoan.setLoanHolder(customerInfo);
            customerLoan.setId(c.getCustomerLoanId());
            customerLoan.setProposal(c.getProposal());
            customerLoan.setLoan(c.getLoan());
            customerLoan.setLoanType(c.getLoanType());
            customerLoan.setCurrentStage(c.getCurrentStage());
            customerLoan.setPreviousStageList(c.getPreviousStageList());
            customerLoanList.add(customerLoan);
        });
        return customerLoanList;
    }
}
