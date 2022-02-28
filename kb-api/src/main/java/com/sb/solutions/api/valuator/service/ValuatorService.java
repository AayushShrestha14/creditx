package com.sb.solutions.api.valuator.service;

import java.util.Collection;
import java.util.Map;

import com.sb.solutions.api.valuator.entity.Valuator;
import com.sb.solutions.core.service.BaseService;

public interface ValuatorService extends BaseService<Valuator> {

    Map<Object, Object> valuatorStatusCount();

    Collection<Valuator> getValuatorFilterBySearch(Object search);

}
