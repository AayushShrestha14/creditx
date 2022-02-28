package com.sb.solutions.api.loan.dto;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customerRelative.entity.CustomerRelative;
import com.sb.solutions.core.enums.Gender;
import com.sb.solutions.core.enums.MaritalStatus;
import com.sb.solutions.core.enums.Status;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CustomerDto {

    private String profilePic;
    private String customerName;
    private Date dob;
    private Province province;
    private District district;
    private MunicipalityVdc municipalities;
    private String street;
    private String wardNumber;
    private String contactNumber;
    private String occupation;
    private String incomeSource;
    private String email;
    private Date initialRelationDate;
    private String citizenshipNumber;
    private Date citizenshipIssuedDate;
    private String citizenshipIssuedPlace;
    private Status status = Status.ACTIVE;
    private String introduction;
    private String individualJsonData;
    private Double netWorth;
    private String withinLimitRemarks;
    private String bankingRelationship;
    private String customerCode;
    private ClientType clientType;
    private String subsectorDetail;
    private String landLineNumber;
    private Province temporaryProvince;
    private District temporaryDistrict;
    private MunicipalityVdc temporaryMunicipalities;
    private String temporaryStreet;
    private String temporaryWardNumber;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String customerLegalDocumentAddress;
    private List<CustomerRelative> customerRelatives;
    private String nepaliDetail;
    //    private Boolean isMicroCustomer;
    private String jointInfo;
//    private Boolean isJointCustomer;
}
