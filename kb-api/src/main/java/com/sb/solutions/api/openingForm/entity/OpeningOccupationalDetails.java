package com.sb.solutions.api.openingForm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpeningOccupationalDetails {

    private String nameOfOrganization;
    private String address;
    private String telNo;
    private String natureOfBusiness;
    private String designation;
    private String estimatedAnnualIncome;
}
