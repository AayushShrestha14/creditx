package com.sb.solutions.api.collateralSiteVisit.service;

import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import com.sb.solutions.core.service.BaseService;

/**
 * Created by Mohammad Hussain on Jun, 2021
 */
public interface SiteVisitDocumentService extends BaseService<SiteVisitDocument> {

    public SiteVisitDocument saveSiteVisitDocumentById(Long id, SiteVisitDocument siteVisitDocument);
}
