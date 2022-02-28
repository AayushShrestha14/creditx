package com.sb.solutions.api.crg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.crg.entity.CrgAnswer;
import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.api.crg.repository.CrgQuestionRepository;
import com.sb.solutions.core.enums.Status;

@Service
@Transactional
@RequiredArgsConstructor
public class CrgQuestionServiceImpl implements CrgQuestionService {

    private final Logger logger = LoggerFactory.getLogger(CrgQuestionServiceImpl.class);

    private final CrgQuestionRepository questionRepository;

    private final CrgAnswerServiceImpl answerService;


    @Override
    public List<CrgQuestion> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public CrgQuestion findOne(Long id) {
        return questionRepository.getOne(id);
    }

    @Override
    public CrgQuestion save(CrgQuestion question) {
        return questionRepository.save(question);
    }

    @Override
    public List<CrgQuestion> save(List<CrgQuestion> questions) {
        List<CrgQuestion> savedQuestions = new ArrayList<>();
        for (CrgQuestion question : questions) {
            question.setLastModifiedAt(new Date());
            question.setStatus(Status.ACTIVE);
            final CrgQuestion savedQuestion = questionRepository.save(question);
            question.getAnswers().forEach(answer -> answer.setCrgQuestion(savedQuestion));
            savedQuestion.setAnswers(answerService.save(question.getAnswers()));
            savedQuestion.setMaximumPoints(
                question.getAnswers().stream().mapToDouble(value -> value.getPoints()).sum());
            savedQuestions.add(questionRepository.save(savedQuestion));
        }
        return savedQuestions;
    }

    @Override
    public Page<CrgQuestion> findAllPageable(Object question, Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public List<CrgQuestion> saveAll(List<CrgQuestion> list) {
        return questionRepository.saveAll(list);
    }

    @Override
    public List<CrgQuestion> findByLoanConfigId(Long loanConfigId) {
        return questionRepository.findByLoanConfigIdAndStatusNotOrderByCrgGroupIdAsc(loanConfigId, Status.DELETED);
    }

    @Override
    public CrgQuestion update(CrgQuestion question) {
        final List<CrgAnswer> answers = new ArrayList<>();
        answers.addAll(question.getAnswers());
        question.setLastModifiedAt(new Date());
        question.getAnswers().removeAll(question.getAnswers());
        CrgQuestion updatedQuestion = questionRepository.save(question);
        for (CrgAnswer answer : answers) {
            answer.setCrgQuestion(updatedQuestion);
        }
        final List<CrgAnswer> updatedAnswers = answerService.update(answers, updatedQuestion);
        updatedQuestion.setMaximumPoints(
            updatedAnswers.stream().mapToDouble(value -> value.getPoints()).max().orElse(0d));
        updatedQuestion.setAnswers(updatedAnswers);
        updatedQuestion = questionRepository.save(updatedQuestion);

        return updatedQuestion;
    }

    @Override
    public void delete(long id) {
        logger.debug("Setting status to deleted for the question with id [{}].", id);
        final CrgQuestion question = questionRepository.getOne(id);
        if (question != null) {
            final List<CrgAnswer> answers = question.getAnswers();
            answerService.delete(answers);
            question.setStatus(Status.DELETED);
            questionRepository.save(question);
        }
    }
}
