package com.sb.solutions.api.financial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.financial.entity.Financial;

public interface FinancialRepository extends JpaRepository<Financial, Long> {

}
