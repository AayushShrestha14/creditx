package com.sb.solutions.api.loan.dto;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.core.enums.Gender;
import com.sb.solutions.core.enums.MaritalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class CustomerListDto {

    private Long id;
    private String name;
    private String idNumber;
    private CustomerType customerType;
    private String contactNo;
    private String email;
    private String idRegPlace;
    private Date idRegDate;
    private Date createdAt;
    private Long associateId;
    private Long groupId;
    private Long provinceId;
    private CustomerGroup customerGroup;
    private Branch branch;
    private ClientType clientType;
    private String subsectorDetail;
    private String customerCode;
    private String bankingRelationship;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String customerLegalDocumentAddress;
    private Long branchIds;
    public CustomerListDto(Long id, String name, String idNumber,
                           CustomerType customerType, String contactNo,
                           String email, String idRegPlace, Date idRegDate,
                           Date createdAt, Long associateId,Long provinceId, Long groupId,
                           ClientType clientType,
                            String subsectorDetail, String customerCode, String bankingRelationship,
                           Gender gender,MaritalStatus maritalStatus, String customerLegalDocumentAddress,
                          Branch branch , Long branchIds) {
        this.id = id;
        this.name = name;
        this.idNumber = idNumber;
        this.customerType = customerType;
        this.contactNo = contactNo;
        this.email = email;
        this.idRegPlace = idRegPlace;
        this.idRegDate = idRegDate;
        this.createdAt = createdAt;
        this.associateId = associateId;
        this.provinceId = provinceId;
        this.groupId = groupId;
        this.clientType = clientType;
        this.subsectorDetail = subsectorDetail;
        this.customerCode = customerCode;
        this.bankingRelationship = bankingRelationship;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.customerLegalDocumentAddress = customerLegalDocumentAddress;
        this.branch = branch;
        this.branchIds = branchIds;
    }
}
