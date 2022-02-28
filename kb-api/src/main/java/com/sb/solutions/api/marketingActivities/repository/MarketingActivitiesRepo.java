package com.sb.solutions.api.marketingActivities.repository;

import com.sb.solutions.api.marketingActivities.MarketingActivities;
import com.sb.solutions.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketingActivitiesRepo extends BaseRepository<MarketingActivities , Long> {
}
