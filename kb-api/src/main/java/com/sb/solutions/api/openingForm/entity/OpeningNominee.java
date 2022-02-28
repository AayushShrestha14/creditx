package com.sb.solutions.api.openingForm.entity;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningNominee {

    private String fullName;
    private String imagePath;
    private String relationToMe;
    private Date dateOfBirth;
    private String citizenNumber;
    private String issuedPlace;
    private String citizenImagePath;
    private String temporaryAddress;
    private String permanentAddress;
    private String contactNumber;
    private Set<OpeningCustomerRelative> nomineeFamily;
}
