package com.sb.solutions.api.eligibility.applicant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.eligibility.applicant.entity.Applicant;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long>,
    JpaSpecificationExecutor<Applicant> {

}
