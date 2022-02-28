package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgGroup;
import com.sb.solutions.core.service.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */
public interface CrgGroupService extends Service<CrgGroup, Long> {

    Page<CrgGroup> findPageable(Map<String, String> filterParams, Pageable pageable);

}
