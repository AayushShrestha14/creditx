package com.sb.solutions.api.sector.subsector.service;

import java.util.Map;

import com.sb.solutions.api.sector.subsector.entity.SubSector;
import com.sb.solutions.core.service.BaseService;

public interface SubSectorService extends BaseService<SubSector> {

    Map<Object, Object> subSectorStatusCount();
}
