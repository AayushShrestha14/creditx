package com.sb.solutions.api.cbsGroup.repository;

import java.util.List;

import com.sb.solutions.api.cbsGroup.entity.CbsGroup;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/
public interface CbsGroupRepository extends BaseRepository<CbsGroup, Long> {

    List<CbsGroup> findCbsGroupByObligor(String obligor);

}
