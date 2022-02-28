package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgAnswer;
import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.core.service.BaseService;

import java.util.List;

public interface CrgAnswerService extends BaseService<CrgAnswer> {

    List<CrgAnswer> save(List<CrgAnswer> answers);

    List<CrgAnswer> update(List<CrgAnswer> answers, CrgQuestion question);

    List<CrgAnswer> findByIds(List<Long> ids);

    void delete(long id);

    void delete(List<CrgAnswer> answers);
}
