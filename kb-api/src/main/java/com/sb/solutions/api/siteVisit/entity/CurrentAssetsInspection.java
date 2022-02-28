package com.sb.solutions.api.siteVisit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAssetsInspection {

    private Boolean uptoDateWithCharges;

    private Boolean borrowersPossession;

    private Boolean notUnderTR;

    private Boolean otherBankNotInterested;

    private Boolean securityOrder;

    private Boolean goodsSaleable;

    private Boolean stocksUptoDate;

    private Boolean matchWithTheStockList;

    private Boolean storageConditionSatisfactory;

    private Boolean fireFightingEvidence;

    private Boolean buildingStoreCondition;

    private Boolean warrantiesUptoDate;

    private Boolean noHazardousNature;

    private Boolean nameBoardProperlyDisplayed;

    private Boolean padlocksUse;

    private String findingAndComments;

    private String remarksForNoOption;

}
