package com.sb.solutions.api.insurance.service;

import java.util.Optional;

import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperService;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.core.service.Service;

public interface InsuranceService extends Service<Insurance, Long>,
    HelperService<Optional<HelperDto<Long>>> {

}
