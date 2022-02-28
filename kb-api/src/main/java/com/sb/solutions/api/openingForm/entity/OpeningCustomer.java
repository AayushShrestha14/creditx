package com.sb.solutions.api.openingForm.entity;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningCustomer {

    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String maritalStatus;
    private String gender;
    private OpeningKyc kyc;
    private String permanentProvince;
    private String permanentDistrict;
    private String permanentMunicipality;
    private String permanentWard;
    private String permanentStreet;
    private String permanentHouseNumber;
    private String presentProvince;
    private String presentDistrict;
    private String presentMunicipality;
    private String presentWard;
    private String presentStreet;
    private String presentHouseNumber;
    private String landLordName;
    private String landLordContactNo;
    private String email;
    private String residentialContactNo;
    private String officeContactNo;
    private String mobileContactNo;
    private Date dateOfBirthBS;
    private Date dateOfBirthAD;
    private String nationality;
    private String citizenNumber;
    private String citizenIssuedPlace;
    private Date citizenIssuedDate;
    private String voterNumber;
    private String voterIssuedPlace;
    private Date voterIssuedDate;
    private String licenseNumber;
    private String licenseIssuedPlace;
    private Date licenseIssuedDate;
    private Date licenseExpireDate;
    private String passportNumber;
    private String passportIssuedPlace;
    private Date passportIssuedDate;
    private Date passportExpireDate;
    private String idCardNumber;
    private String idCardIssueAuthority;
    private Date idCardIssuedDate;
    private Date idCardExpireDate;
    private Date visaIssueDate;
    private Date visaValidity;
    private String panNo;
    private String salariedEmployedWith;
    private String selfEmployedWith;
    private String otherSourceOfIncome;
    private boolean accountInAnotherBank;
    private String bankName;
    private String education;
    private String map;
    private boolean usResident;
    private boolean usCitizen;
    private boolean greenCardHolder;
    private boolean exposeToPep;
    private String pepName;
    private String pepDesignation;
    private boolean convictedOfCrime;
    private String convictedCrime;
    private boolean residentialPermitOfForeign;
    private String residentialPermitOfForeignType;
    private boolean highProfileRelation;
    private String residentialPermitOfForeignCountryName;
    private Set<AccountOpeningDocument> documents;
}
