package com.sb.solutions.api.eligibility.question.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.eligibility.answer.service.AnswerService;
import com.sb.solutions.api.eligibility.question.entity.Question;
import com.sb.solutions.api.eligibility.question.repository.QuestionRepository;
import com.sb.solutions.api.eligibility.question.service.QuestionService;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.core.enums.Status;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final AnswerService answerService;

    private final LoanConfigService loanConfigService;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerService answerService,
        LoanConfigService loanConfigService) {
        this.questionRepository = questionRepository;
        this.answerService = answerService;
        this.loanConfigService = loanConfigService;
    }

    @Override
    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public Question findOne(Long id) {
        return questionRepository.getOne(id);
    }

    @Override
    public Question save(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> save(List<Question> questions) {
        List<Question> savedQuestions = new ArrayList<>();
        for (Question question : questions) {
            question.setLastModifiedAt(new Date());
            question.setStatus(Status.ACTIVE);
            final Question savedQuestion = questionRepository.save(question);
            question.getAnswers().forEach(answer -> answer.setQuestion(savedQuestion));
            savedQuestion.setAnswers(answerService.save(question.getAnswers()));
            savedQuestion.setMaximumPoints(question.getAnswers().stream()
                .map(Answer::getPoints).max(Comparator.comparing(Long::valueOf)).get());
            savedQuestions.add(questionRepository.save(savedQuestion));
        }
        LoanConfig loanConfig = loanConfigService.findOne(savedQuestions.stream()
            .map(Question::getLoanConfig).distinct().findAny().orElse(null).getId());
        loanConfig.setTotalPoints(
            loanConfig.getTotalPoints() + savedQuestions.stream().map(Question::getMaximumPoints)
                .mapToLong(Long::longValue).sum());
        loanConfigService.save(loanConfig);
        return savedQuestions;
    }

    @Override
    public Page<Question> findAllPageable(Object question, Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    @Override
    public List<Question> saveAll(List<Question> list) {
        return questionRepository.saveAll(list);
    }

    @Override
    public List<Question> findByLoanConfigId(Long loanConfigId) {
        return questionRepository.findByLoanConfigIdAndStatusNot(loanConfigId, Status.DELETED);
    }

    @Override
    public Question update(Question question) {
        final List<Answer> answers = new ArrayList<>();
        answers.addAll(question.getAnswers());
        question.setLastModifiedAt(new Date());
        question.getAnswers().removeAll(question.getAnswers());
        Question updatedQuestion = questionRepository.save(question);
        for (Answer answer : answers) {
            answer.setQuestion(updatedQuestion);
        }
        final List<Answer> updatedAnswers = answerService.update(answers, updatedQuestion);
        updatedQuestion.setMaximumPoints(updatedAnswers.stream().map(Answer::getPoints)
            .max(Comparator.comparing(Long::valueOf)).orElse(0L));
        updatedQuestion.setAnswers(updatedAnswers);
        updatedQuestion = questionRepository.save(updatedQuestion);
        LoanConfig loanConfig = loanConfigService.findOne(updatedQuestion.getLoanConfig().getId());
        List<Question> allQuestions = findByLoanConfigId(loanConfig.getId());
        loanConfig.setTotalPoints(
            allQuestions.stream().map(Question::getMaximumPoints).mapToLong(Long::longValue).sum());
        loanConfigService.save(loanConfig);
        return updatedQuestion;
    }

    @Override
    public void delete(long id) {
        logger.debug("Setting status to deleted for the question with id [{}].", id);
        final Question question = questionRepository.getOne(id);
        if (question != null) {
            final List<Answer> answers = question.getAnswers();
            answerService.delete(answers);
            question.setStatus(Status.DELETED);
            questionRepository.save(question);
        }
    }
}
