package com.sb.solutions.api.collateralSiteVisit.service;

import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import com.sb.solutions.api.collateralSiteVisit.repository.SiteVisitDocumentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Mohammad Hussain on Jun, 2021
 */
@Service
public class SiteVisitDocumentServiceImpl implements SiteVisitDocumentService {

    private SiteVisitDocumentRepository repository;
    private CollateralSiteVisitService collateralSiteVisitService;

    public SiteVisitDocumentServiceImpl(SiteVisitDocumentRepository repository,
                                        CollateralSiteVisitService collateralSiteVisitService) {
        this.repository = repository;
        this.collateralSiteVisitService = collateralSiteVisitService;
    }

    @Override
    public List<SiteVisitDocument> findAll() {
        return repository.findAll();
    }

    @Override
    public SiteVisitDocument findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public SiteVisitDocument save(SiteVisitDocument siteVisitDocument) {
        return repository.save(siteVisitDocument);
    }

    @Override
    public Page<SiteVisitDocument> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<SiteVisitDocument> saveAll(List<SiteVisitDocument> list) {
        return repository.saveAll(list);
    }

    @Override
    public SiteVisitDocument saveSiteVisitDocumentById(Long id, SiteVisitDocument siteVisitDocument) {
        return repository.save(siteVisitDocument);
    }
}
