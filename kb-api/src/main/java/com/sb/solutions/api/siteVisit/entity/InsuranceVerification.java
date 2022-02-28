package com.sb.solutions.api.siteVisit.entity;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceVerification {

    private String assetsMortgaged;

    private double insuredAmount;

    private String insuranceCompany;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private String clientRating;

    private String comments;

    private double stockValueConfirmed;

    private List<InspectingStaff> inspectingStaffList;

}

