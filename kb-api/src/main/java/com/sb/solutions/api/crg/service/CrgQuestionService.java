package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.core.service.BaseService;

import java.util.List;

public interface CrgQuestionService extends BaseService<CrgQuestion> {

    List<CrgQuestion> save(List<CrgQuestion> questions);

    List<CrgQuestion> findByLoanConfigId(Long loanConfigId);

    CrgQuestion update(CrgQuestion question);

    void delete(long id);
}
