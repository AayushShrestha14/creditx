package com.sb.solutions.api.reportinginfo.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfo;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author Elvin Shrestha on 3/27/2020
 */
@Repository
public interface ReportingInfoRepository extends BaseRepository<ReportingInfo, Long> {

}
