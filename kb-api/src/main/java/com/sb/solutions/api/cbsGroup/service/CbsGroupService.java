package com.sb.solutions.api.cbsGroup.service;

import java.util.List;

import com.sb.solutions.api.cbsGroup.entity.CbsGroup;
import com.sb.solutions.core.service.BaseService;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/
public interface CbsGroupService extends BaseService<CbsGroup> {

    List<CbsGroup> findCbsGroupByObl(String obligor);
}
