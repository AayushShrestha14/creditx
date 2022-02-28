package com.sb.solutions.api.loan.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.loan.entity.CombinedLoan;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
@Repository
public interface CombinedLoanRepository extends BaseRepository<CombinedLoan, Long> {

}
