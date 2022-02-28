package com.sb.solutions.api.nepseCompany.repository;

import com.sb.solutions.api.nepseCompany.entity.NepsePriceInfo;
import com.sb.solutions.core.repository.BaseRepository;

public interface NepsePriceInfoRepository extends BaseRepository<NepsePriceInfo, Long> {
    NepsePriceInfo findFirstByOrderByIdDesc();
}
