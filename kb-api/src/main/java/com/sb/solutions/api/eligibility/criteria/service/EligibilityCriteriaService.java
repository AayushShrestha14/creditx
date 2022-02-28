package com.sb.solutions.api.eligibility.criteria.service;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;

public interface EligibilityCriteriaService extends BaseService<EligibilityCriteria> {

    void delete(Long id);

    EligibilityCriteria update(EligibilityCriteria eligibilityCriteria, Long id);

    EligibilityCriteria getByStatus(Status status);

}
