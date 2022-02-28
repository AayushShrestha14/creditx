package com.sb.solutions.api.siteVisit.service;

import java.util.List;

import com.sb.solutions.api.siteVisit.entity.SiteVisit;
import com.sb.solutions.core.service.BaseService;

public interface SiteVisitService extends BaseService<SiteVisit> {
    List<SiteVisit> saveAll(List<SiteVisit> siteVisits);
}
