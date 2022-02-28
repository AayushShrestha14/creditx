package com.sb.solutions.api.collateralSiteVisit.service;

import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import com.sb.solutions.api.collateralSiteVisit.repository.CollateralSiteVisitRepository;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.api.security.service.SecurityService;
import com.sb.solutions.core.utils.file.DeleteFileUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mohammad Hussain on May, 2021
 */
@Service
public class CollateralSiteVisitServiceImpl implements CollateralSiteVisitService {

    private final CollateralSiteVisitRepository repository;
    private final SecurityService securityService;
    public final Logger logger = LoggerFactory.getLogger(CollateralSiteVisitServiceImpl.class);
    static final String FILE_TYPE = ".jpg";

    public CollateralSiteVisitServiceImpl(CollateralSiteVisitRepository repository,
                                          SecurityService securityService) {
        this.repository = repository;
        this.securityService = securityService;
    }

    @Override
    public List<CollateralSiteVisit> findAll() {
        return repository.findAll();
    }

    @Override
    public CollateralSiteVisit findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public CollateralSiteVisit save(CollateralSiteVisit collateralSiteVisit) {
        return repository.save(collateralSiteVisit);
    }

    @Override
    public Page<CollateralSiteVisit> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<CollateralSiteVisit> saveAll(List<CollateralSiteVisit> collateralSiteVisits) {
        return repository.saveAll(collateralSiteVisits);
    }

    @Override
    public CollateralSiteVisit saveCollateralSiteVisit(Long securityId, CollateralSiteVisit collateralSiteVisit) {
        Security security = securityService.findOne(securityId);
        if (security != null) {
            collateralSiteVisit.setSecurity(security);
        }
        return repository.save(collateralSiteVisit);
    }

    @Override
    public List<CollateralSiteVisit> getCollateralSiteVisitBySecurityNameAndSecurityAndId(String securityName, Long id) {
        return repository.findCollateralSiteVisitBySecurityNameAndSecurity_Id(securityName, id);
    }

    @Override
    public CollateralSiteVisit getCollateralBySiteVisitDateAndId(LocalDate siteVisitDate, Long Id) {
        return repository.findCollateralSiteVisitBySiteVisitDateAndId(siteVisitDate, Id);
    }

    @Override
    public List<CollateralSiteVisit> getCollateralSiteVisitBySecurityId(Long Id) {
        return repository.findCollateralSiteVisitBySecurity_Id(Id);
    }

    @Override
    public void deleteSiteVisit(CollateralSiteVisit collateralSiteVisit) {
        if (collateralSiteVisit != null) {
            collateralSiteVisit.setCollateralDeleted(1);
            List<SiteVisitDocument> siteVisitDocuments = collateralSiteVisit.getSiteVisitDocuments();
            if (siteVisitDocuments.size() > 0) {
                for (SiteVisitDocument siteVisitDocument: siteVisitDocuments) {
                    siteVisitDocument.setDocDeleted(1);
                }
            }
            repository.save(collateralSiteVisit);
        }
    }

    @Override
    public List<CollateralSiteVisit> deleteAllSiteVisit(Long securityId, String securityName) {
        if (securityId != null && securityName != null) {
            List<CollateralSiteVisit> collateralSiteVisits1 = repository.findCollateralSiteVisitBySecurityNameAndSecurity_Id(securityName, securityId);
            List<SiteVisitDocument> siteVisitDocuments = new ArrayList<>();
            for (CollateralSiteVisit collateralSiteVisit: collateralSiteVisits1) {
                collateralSiteVisit.setCollateralDeleted(1);
                siteVisitDocuments.addAll(collateralSiteVisit.getSiteVisitDocuments());
            }
            if (siteVisitDocuments.size()> 0) {
                for (SiteVisitDocument siteVisitDocument: siteVisitDocuments) {
                    siteVisitDocument.setDocDeleted(1);
                }
            }
            repository.saveAll(collateralSiteVisits1);
        }
        return null;
    }
}
