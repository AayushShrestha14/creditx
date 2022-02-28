package com.sb.solutions.api.creditRiskGrading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.creditRiskGrading.entity.CreditRiskGrading;

/**
 * @author yunish on 11/22/2019
 */
public interface CreditRiskGradingRepository extends JpaRepository<CreditRiskGrading, Long> {

}
