package com.sb.solutions.api.eligibility.answer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.eligibility.answer.repository.AnswerRepository;
import com.sb.solutions.api.eligibility.answer.service.AnswerService;
import com.sb.solutions.api.eligibility.question.entity.Question;
import com.sb.solutions.core.enums.Status;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final Logger logger = LoggerFactory.getLogger(AnswerServiceImpl.class);

    private final AnswerRepository answerRepository;

    public AnswerServiceImpl(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Answer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public Answer findOne(Long id) {
        return answerRepository.getOne(id);
    }

    @Override
    public Answer save(Answer answer) {
        answer.setLastModifiedAt(new Date());
        return answerRepository.save(answer);
    }

    @Override
    public List<Answer> save(List<Answer> answers) {
        answers.forEach(answer -> {
            answer.setLastModifiedAt(new Date());
            answer.setStatus(Status.ACTIVE);
        });
        return answerRepository.saveAll(answers);
    }

    @Override
    public Page<Answer> findAllPageable(Object answer, Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    @Override
    public List<Answer> saveAll(List<Answer> list) {
        return answerRepository.saveAll(list);
    }


    @Transactional
    @Override
    public List<Answer> update(List<Answer> answers, Question question) {
        final List<Answer> savedAnswers = answerRepository
            .findAllByQuestionIdAndStatusNot(question.getId(),
                Status.DELETED);
        final List<Answer> newAnswers = answers.stream()
            .filter(answer -> answer.getId() == null || answer.getId() == 0)
            .collect(Collectors.toList());
        final List<Answer> modifiedAnswers = answers.stream()
            .filter(answer -> !newAnswers.contains(answer)
                && savedAnswers.stream().map(Answer::getId).collect(Collectors.toList())
                .contains(answer.getId()))
            .collect(Collectors.toList());
        final List<Answer> deletedAnswers = savedAnswers.stream()
            .filter(answer -> modifiedAnswers.stream()
                .noneMatch(modifiedAnswer -> answer.getId().equals(modifiedAnswer.getId())))
            .collect(Collectors.toList());
        List<Answer> answersToPersist = new ArrayList<>();
        answersToPersist.addAll(newAnswers);
        answersToPersist.addAll(modifiedAnswers);
        answersToPersist.forEach(answerToPersist -> answerToPersist.setLastModifiedAt(new Date()));
        delete(deletedAnswers);
        answersToPersist = answerRepository.saveAll(answersToPersist);
        return answersToPersist;
    }

    @Override
    public List<Answer> findByIds(List<Long> ids) {
        logger.debug("Finding answers with supplied list ids.");
        return answerRepository.findByIdIn(ids);
    }

    @Override
    public void delete(long id) {
        logger.debug("Setting status to deleted for the answer with id [{}].", id);
        final Answer answer = answerRepository.getOne(id);
        if (answer != null) {
            answer.setStatus(Status.DELETED);
            answerRepository.save(answer);
        }
    }

    @Override
    public void delete(List<Answer> answers) {
        logger.debug("Setting status to deleted for the list of answers.");
        answers.forEach(answer -> answer.setStatus(Status.DELETED));
        answerRepository.saveAll(answers);
    }
}
