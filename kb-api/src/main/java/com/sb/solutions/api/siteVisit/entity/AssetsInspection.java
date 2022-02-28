package com.sb.solutions.api.siteVisit.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsInspection {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfInspection;

    private String goodsParticular;

    private double stockValueReported;

    private String premisesOwnedRented;

    private String rentPmtUptoDate;

    private String rentReceiptShown;

    private InsuranceVerification insuranceVerification;
}
