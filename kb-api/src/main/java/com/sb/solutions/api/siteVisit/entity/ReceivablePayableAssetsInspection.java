package com.sb.solutions.api.siteVisit.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivablePayableAssetsInspection {

    private List<PartyInfo> partyInfoList;

    private double threeMonthTotal;

    private double sixMonthTotal;

    private double oneYearTotal;

    private double moreThanOneYearTotal;

    private String findingsAndCommentsForCurrentAssetsInspection;

    private List<ReceivableCurrentAssets> receivableCurrentAssetsList;

    private Double receivableCurrentAssetsTotal;

    private List<PayableCurrentAssets> payableCurrentAssetsList;

    private Double payableCurrentAssetsTotal;

    private List<InspectingStaff> inspectingStaffList;

    private List<BankExposureAssets> bankExposureAssetsList;

    private String overallFindingAndCommentsOfCAI;

}
