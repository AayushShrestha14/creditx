package com.sb.solutions.api.siteVisit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.siteVisit.entity.SiteVisit;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long> {

}
