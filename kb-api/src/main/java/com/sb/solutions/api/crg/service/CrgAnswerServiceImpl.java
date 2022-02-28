package com.sb.solutions.api.crg.service;

import com.sb.solutions.api.crg.entity.CrgAnswer;
import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.api.crg.repository.CrgAnswerRepository;
import com.sb.solutions.core.enums.Status;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrgAnswerServiceImpl implements CrgAnswerService {

    private final Logger logger = LoggerFactory.getLogger(CrgAnswerServiceImpl.class);

    private final CrgAnswerRepository answerRepository;


    @Override
    public List<CrgAnswer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public CrgAnswer findOne(Long id) {
        return answerRepository.getOne(id);
    }

    @Override
    public CrgAnswer save(CrgAnswer answer) {
        answer.setLastModifiedAt(new Date());
        return answerRepository.save(answer);
    }

    @Override
    public List<CrgAnswer> save(List<CrgAnswer> answers) {
        answers.forEach(answer -> {
            answer.setLastModifiedAt(new Date());
            answer.setStatus(Status.ACTIVE);
        });
        return answerRepository.saveAll(answers);
    }

    @Override
    public Page<CrgAnswer> findAllPageable(Object answer, Pageable pageable) {
        return answerRepository.findAll(pageable);
    }

    @Override
    public List<CrgAnswer> saveAll(List<CrgAnswer> list) {
        return answerRepository.saveAll(list);
    }


    @Transactional
    @Override
    public List<CrgAnswer> update(List<CrgAnswer> answers, CrgQuestion question) {
        final List<CrgAnswer> savedAnswers = answerRepository
                .findAllByCrgQuestionIdAndStatusNot(question.getId(),
                        Status.DELETED);
        final List<CrgAnswer> newAnswers = answers.stream()
                .filter(answer -> answer.getId() == null || answer.getId() == 0)
                .collect(Collectors.toList());
        final List<CrgAnswer> modifiedAnswers = answers.stream()
                .filter(answer -> !newAnswers.contains(answer)
                        && savedAnswers.stream().map(CrgAnswer::getId).collect(Collectors.toList())
                        .contains(answer.getId()))
                .collect(Collectors.toList());
        final List<CrgAnswer> deletedAnswers = savedAnswers.stream()
                .filter(answer -> modifiedAnswers.stream()
                        .noneMatch(modifiedAnswer -> answer.getId().equals(modifiedAnswer.getId())))
                .collect(Collectors.toList());
        List<CrgAnswer> answersToPersist = new ArrayList<>();
        answersToPersist.addAll(newAnswers);
        answersToPersist.addAll(modifiedAnswers);
        answersToPersist.forEach(answerToPersist -> answerToPersist.setLastModifiedAt(new Date()));
        delete(deletedAnswers);
        answersToPersist = answerRepository.saveAll(answersToPersist);
        return answersToPersist;
    }

    @Override
    public List<CrgAnswer> findByIds(List<Long> ids) {
        logger.debug("Finding answers with supplied list ids.");
        return answerRepository.findByIdIn(ids);
    }

    @Override
    public void delete(long id) {
        logger.debug("Setting status to deleted for the answer with id [{}].", id);
        final CrgAnswer answer = answerRepository.getOne(id);
        if (answer != null) {
            answer.setStatus(Status.DELETED);
            answerRepository.save(answer);
        }
    }

    @Override
    public void delete(List<CrgAnswer> answers) {
        logger.debug("Setting status to deleted for the list of answers.");
        answers.forEach(answer -> answer.setStatus(Status.DELETED));
        answerRepository.saveAll(answers);
    }
}
