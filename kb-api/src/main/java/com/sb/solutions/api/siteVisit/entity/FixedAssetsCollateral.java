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
public class FixedAssetsCollateral {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String address;

    private String nameOfPersonContacted;

    private String personContactedPhone;

    private String roadApproach;

    private Double roadWidth;

    private String prominentPlace;

    private Double approachDistance;

    //Others facility
    private Boolean waterSupply;

    private Boolean electricity;

    private Boolean boundryWallConstruction;

    private Boolean boundryFencing;

    private Boolean drainage;

    private Boolean open;

    private String remarksForOtherFacility;

    private Boolean building;

    //Quality of construction
    private Double buildingArea;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBuildingConstruction;

    private String qualityOfConstructionRemarks;

    private Boolean loadBearingWall;

    private Boolean mortarCement;

    private Boolean otherRoofing;

    private Boolean insideFurniture;

    private Boolean frameStructure;

    private Boolean rccRoofing;

    private Boolean bathroomToilet;

    private Double majorMarketplacesDistance;

    private Double schoolCollegeDistance;

    private Double hospitalNursingHomeDistance;

    private Double electricityLineDistance;

    private Double telephoneLineDistance;

    private Double waterPipelineDistance;

    private List<InspectingStaff> inspectingStaffList;

    private String commentsAboutFAC;

    private String branchInchargeComment;

}

