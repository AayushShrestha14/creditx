package com.sb.solutions.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customer.enums.CustomerType;

/**
 * @author : Rujan Maharjan on  12/1/2020
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanHolderDto {

    private Long id = 0L;
    private CustomerType customerType;
    private String name;

    private Long associateId;
    private String idNumber;
    private Date idRegDate;
    private String idRegPlace;
    private String branchName;
    private String provinceName;
    private Long branchId;
    private List<CustomerLoanDto> customerLoanDtoList = new ArrayList<>();
    private Long totalLoan = 0L;
    private Long cadId;
    private Branch branch;
    private String customerLoanIds;

    public LoanHolderDto(Long id, CustomerType customerType, String name, Long associateId,
        String idNumber, Date idRegDate, String idRegPlace, String branchName,
        String provinceName, Long branchId) {
        this.id = id;
        this.customerType = customerType;
        this.name = name;
        this.associateId = associateId;
        this.idNumber = idNumber;
        this.idRegDate = idRegDate;
        this.idRegPlace = idRegPlace;
        this.branchName = branchName;
        this.provinceName = provinceName;
        this.branchId = branchId;
    }

    public Branch getBranch() {
        Branch branch = new Branch();
        if (branchId == null) {
            return this.branch;
        }
        Province p = new Province();
        p.setName(this.provinceName);
        branch.setId(branchId);
        branch.setName(branchName);
        branch.setProvince(p);
        this.branch = branch;
        return this.branch;
    }
}
