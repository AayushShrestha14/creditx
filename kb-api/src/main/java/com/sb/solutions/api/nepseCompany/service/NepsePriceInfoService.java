package com.sb.solutions.api.nepseCompany.service;

import com.sb.solutions.api.nepseCompany.entity.NepsePriceInfo;
import com.sb.solutions.core.service.BaseService;

public interface NepsePriceInfoService extends BaseService<NepsePriceInfo>{
    NepsePriceInfo getActivePriceInfo();
    void deletePrevRecord();
}
