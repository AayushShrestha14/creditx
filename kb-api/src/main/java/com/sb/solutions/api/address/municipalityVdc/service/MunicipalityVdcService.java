package com.sb.solutions.api.address.municipalityVdc.service;

import java.util.List;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.core.service.BaseService;

public interface MunicipalityVdcService extends BaseService<MunicipalityVdc> {

    List<MunicipalityVdc> findAllByDistrict(District district);
}
