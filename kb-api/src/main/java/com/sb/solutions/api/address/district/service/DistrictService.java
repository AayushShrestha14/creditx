package com.sb.solutions.api.address.district.service;

import java.util.List;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.core.service.BaseService;

public interface DistrictService extends BaseService<District> {

    List<District> findAllByProvince(Province province);
}
