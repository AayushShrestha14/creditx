package com.sb.solutions.api.microBorrowerFinancial.repository;

import com.sb.solutions.api.microBorrowerFinancial.MicroBorrowerFinancial;
import com.sb.solutions.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MicroBorrowerFinancialRepo extends BaseRepository<MicroBorrowerFinancial , Long> {
}
