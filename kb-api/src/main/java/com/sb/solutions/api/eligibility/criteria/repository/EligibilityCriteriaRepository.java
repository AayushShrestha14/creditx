package com.sb.solutions.api.eligibility.criteria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.core.enums.Status;

@Repository
public interface EligibilityCriteriaRepository extends JpaRepository<EligibilityCriteria, Long>,
    JpaSpecificationExecutor<EligibilityCriteria> {

    EligibilityCriteria findByStatus(Status status);
}
