package com.sb.solutions.api.insurance.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.core.repository.BaseRepository;

@Repository
public interface InsuranceRepository extends BaseRepository<Insurance, Long> {

}
