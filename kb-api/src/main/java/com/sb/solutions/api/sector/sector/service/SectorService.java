package com.sb.solutions.api.sector.sector.service;

import java.util.Map;

import com.sb.solutions.api.sector.sector.entity.Sector;
import com.sb.solutions.core.service.BaseService;

public interface SectorService extends BaseService<Sector> {

    Map<Object, Object> sectorStatusCount();
}
