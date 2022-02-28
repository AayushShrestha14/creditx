package com.sb.solutions.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.utils.FilterJsonUtils;
import com.sb.solutions.entity.CadStage;
import com.sb.solutions.enums.CadDocStatus;

/**
 * @author : Rujan Maharjan on  8/5/2021
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CadListDto {

    private Long id;

    private CustomerInfo loanHolder;

    private CadStage cadCurrentStage;

    private String cadStageList;

    private CadDocStatus docStatus;

    private List<CadStage> previousList;

    private Long branchId;

    private String branchName;

    private String provinceName;

    private String customerName;

    private CustomerType customerType;
    private ClientType clientType;


    private long count;
    private List<CustomerLoan> assignedLoan;
    private Boolean isAdditionalDisbursement;
    private Date lastModifiedAt;
    private String name;

    public CadListDto(Long id,
        CadStage cadCurrentStage, String cadStageList, CadDocStatus docStatus, Long branchId,
        String branchName,
        String provinceName, String customerName, CustomerType customerType,
        ClientType clientType,  Boolean isAdditionalDisbursement,Date lastModifiedAt,String name
    ) {
        this.id = id;
        this.cadCurrentStage = cadCurrentStage;
        this.cadStageList = cadStageList;
        this.docStatus = docStatus;
        this.branchId = branchId;

        this.branchName = branchName;
        this.provinceName = provinceName;
        this.customerName = customerName;
        this.customerType = customerType;
        this.clientType = clientType;
        this.isAdditionalDisbursement = isAdditionalDisbursement;
        this.lastModifiedAt = lastModifiedAt;
        this.name = name;
    }


    public List<CadStage> getPreviousList() {
        return FilterJsonUtils.stringToJSon(this.cadStageList, CadStage.class);
    }

    public CustomerInfo getLoanHolder() {
        CustomerInfo customerInfo = new CustomerInfo();
        Province p = new Province();
        p.setName(this.provinceName);
        Branch b = new Branch();
        b.setId(this.branchId);
        b.setName(this.branchName);
        b.setProvince(p);
        customerInfo.setBranch(b);
        customerInfo.setName(this.customerName);
        customerInfo.setCustomerType(this.customerType);
        customerInfo.setClientType(this.clientType);
        return customerInfo;
    }
}
