package com.sb.solutions.api.borrowerPortfolio;

import com.sb.solutions.api.borrowerPortfolio.entity.BorrowerPortFolio;
import com.sb.solutions.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerPortFolioRepo extends BaseRepository<BorrowerPortFolio, Long> {
}
