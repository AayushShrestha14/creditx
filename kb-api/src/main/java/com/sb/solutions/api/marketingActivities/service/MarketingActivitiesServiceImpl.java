package com.sb.solutions.api.marketingActivities.service;

import com.sb.solutions.api.marketingActivities.MarketingActivities;
import com.sb.solutions.core.repository.BaseRepository;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MarketingActivitiesServiceImpl extends BaseServiceImpl<MarketingActivities, Long> implements MarketingActivitiesService {
    protected MarketingActivitiesServiceImpl(BaseRepository<MarketingActivities, Long> repository) {
        super(repository);
    }

    @Override
    protected BaseSpecBuilder<MarketingActivities> getSpec(Map<String, String> filterParams) {
        return null;
    }
}
