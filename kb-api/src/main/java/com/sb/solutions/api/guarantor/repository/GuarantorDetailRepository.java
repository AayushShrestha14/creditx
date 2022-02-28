package com.sb.solutions.api.guarantor.repository;

import org.springframework.stereotype.Repository;

import com.sb.solutions.api.guarantor.entity.GuarantorDetail;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author Elvin Shrestha on 2/27/2020
 */
@Repository
public interface GuarantorDetailRepository extends BaseRepository<GuarantorDetail, Long> {

}
