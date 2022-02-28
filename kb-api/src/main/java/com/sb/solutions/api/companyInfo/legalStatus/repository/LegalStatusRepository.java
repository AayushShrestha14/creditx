package com.sb.solutions.api.companyInfo.legalStatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.companyInfo.legalStatus.entity.LegalStatus;

public interface LegalStatusRepository extends JpaRepository<LegalStatus, Long> {

}
