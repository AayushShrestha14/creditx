package com.sb.solutions.api.nepseCompany.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sb.solutions.api.nepseCompany.entity.NepseMaster;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author Sunil Babu Shrestha on 1/17/2020
 */
public interface NepseMasterRepository extends BaseRepository<NepseMaster, Long> {

    NepseMaster findByStatus(Status status);

    Page<NepseMaster> findAllByOrderByIdDesc(Pageable pageble);
}
