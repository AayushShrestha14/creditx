package com.sb.solutions.api.siteVisit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.siteVisit.entity.SiteVisit;
import com.sb.solutions.api.siteVisit.repository.SiteVisitRepository;

@Service
public class SiteVisitServiceImpl implements SiteVisitService {

    final SiteVisitRepository siteVisitRepository;

    @Autowired
    public SiteVisitServiceImpl(SiteVisitRepository siteVisitRepository) {
        this.siteVisitRepository = siteVisitRepository;
    }

    @Override
    public List<SiteVisit> findAll() {
        return siteVisitRepository.findAll();
    }

    @Override
    public SiteVisit findOne(Long id) {
        return siteVisitRepository.getOne(id);
    }

    @Override
    public SiteVisit save(SiteVisit siteVisit) {
        return siteVisitRepository.save(siteVisit);
    }

    @Override
    public Page<SiteVisit> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<SiteVisit> saveAll(List<SiteVisit> siteVisits) {
        return siteVisitRepository.saveAll(siteVisits);
    }
}
