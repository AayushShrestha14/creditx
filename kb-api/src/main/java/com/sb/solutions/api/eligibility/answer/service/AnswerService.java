package com.sb.solutions.api.eligibility.answer.service;

import java.util.List;

import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.eligibility.question.entity.Question;
import com.sb.solutions.core.service.BaseService;

public interface AnswerService extends BaseService<Answer> {

    List<Answer> save(List<Answer> answers);

    List<Answer> update(List<Answer> answers, Question question);

    List<Answer> findByIds(List<Long> ids);

    void delete(long id);

    void delete(List<Answer> answers);
}
