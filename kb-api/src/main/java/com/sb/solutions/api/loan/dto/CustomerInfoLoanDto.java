package com.sb.solutions.api.loan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;

/**
 * @author : Rujan Maharjan on  3/11/2021
 **/

@Data
@NoArgsConstructor
public class CustomerInfoLoanDto {

    private CustomerInfo customerInfo;

    private List<CustomerLoanFilterDto> loanSingleList = new ArrayList<>();
    private List<Map<Long, List<CustomerLoanFilterDto>>> combineList = new ArrayList<>();

    private Long totalLoan = 0L;

    public CustomerInfoLoanDto(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    @JsonProperty(access = Access.WRITE_ONLY)
    private String name;

    @JsonProperty(access = Access.WRITE_ONLY)
    private CustomerType customerType;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Branch branch;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Long id;

    @JsonProperty(access = Access.WRITE_ONLY)
    private ClientType clientType;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String customerCode;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String contactNo;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String branchName;

    @JsonProperty(access = Access.WRITE_ONLY)
    private String branchProvince;

    public CustomerInfoLoanDto(CustomerInfo customerInfo,
        List<CustomerLoanFilterDto> loanSingleList, Long totalLoan) {
        this.customerInfo = customerInfo;
        this.loanSingleList = loanSingleList;
        this.totalLoan = totalLoan;
    }

    public Long getTotalLoan() {
        Long i = (long) this.combineList.size();
        return ((long) this.getLoanSingleList().size()) + i;
    }

    public CustomerInfo getCustomerInfo() {
        CustomerInfo customerInfo = new CustomerInfo();
        Branch branch = new Branch();
        branch.setName(this.branchName);
        Province province = new Province();
        province.setName(this.branchProvince);
        branch.setProvince(province);
        customerInfo.setId(this.id);
        customerInfo.setName(this.name);
        customerInfo.setBranch(branch);
        customerInfo.setCustomerType(this.customerType);
        customerInfo.setClientType(this.clientType);
        customerInfo.setCustomerCode(this.customerCode);
        customerInfo.setContactNo(this.contactNo);
        return customerInfo;

    }

    public CustomerInfoLoanDto(String name,
        CustomerType customerType, Long id,
        ClientType clientType, String customerCode, String contactNo, String branchName,
        String branchProvince) {
        this.name = name;
        this.customerType = customerType;
        this.id = id;
        this.clientType = clientType;
        this.customerCode = customerCode;
        this.contactNo = contactNo;
        this.branchName = branchName;
        this.branchProvince = branchProvince;
    }
}
