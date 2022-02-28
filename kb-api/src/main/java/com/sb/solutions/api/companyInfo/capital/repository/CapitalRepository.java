package com.sb.solutions.api.companyInfo.capital.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.companyInfo.capital.entity.Capital;

public interface CapitalRepository extends JpaRepository<Capital, Long> {

}
