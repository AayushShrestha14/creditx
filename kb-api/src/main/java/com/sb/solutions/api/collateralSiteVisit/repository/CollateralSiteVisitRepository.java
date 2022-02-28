package com.sb.solutions.api.collateralSiteVisit.repository;

import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Mohammad Hussain on May, 2021
 */
public interface CollateralSiteVisitRepository extends JpaRepository<CollateralSiteVisit, Long> {

    @Query(value = "select * from collateral_site_visit where security_name = :securityName and security_id = :Id and collateral_deleted = 0", nativeQuery = true)
    List<CollateralSiteVisit> findCollateralSiteVisitBySecurityNameAndSecurity_Id(String securityName, Long Id);

    CollateralSiteVisit findCollateralSiteVisitBySiteVisitDateAndId(LocalDate siteVisitDate, Long Id);

    @Query(value = "select * from collateral_site_visit where security_id = :Id and collateral_deleted = 0", nativeQuery = true)
    List<CollateralSiteVisit> findCollateralSiteVisitBySecurity_Id(Long Id);
}
