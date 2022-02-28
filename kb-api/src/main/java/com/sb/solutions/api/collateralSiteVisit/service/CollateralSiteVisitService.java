package com.sb.solutions.api.collateralSiteVisit.service;

import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import com.sb.solutions.core.service.BaseService;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Mohammad Hussain on May, 2021
 */
public interface CollateralSiteVisitService extends BaseService<CollateralSiteVisit> {

    CollateralSiteVisit saveCollateralSiteVisit(Long securityId, CollateralSiteVisit collateralSiteVisit);

    List<CollateralSiteVisit> getCollateralSiteVisitBySecurityNameAndSecurityAndId(String securityName, Long Id);

    CollateralSiteVisit getCollateralBySiteVisitDateAndId(LocalDate siteVisitDate, Long Id);

    List<CollateralSiteVisit> getCollateralSiteVisitBySecurityId(Long Id);

    void deleteSiteVisit(CollateralSiteVisit collateralSiteVisit);

    List<CollateralSiteVisit> deleteAllSiteVisit(Long id, String securityName);
}
