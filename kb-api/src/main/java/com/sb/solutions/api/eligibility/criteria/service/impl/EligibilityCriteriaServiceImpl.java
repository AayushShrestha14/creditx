package com.sb.solutions.api.eligibility.criteria.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.api.eligibility.criteria.repository.EligibilityCriteriaRepository;
import com.sb.solutions.api.eligibility.criteria.service.EligibilityCriteriaService;
import com.sb.solutions.api.eligibility.question.entity.EligibilityQuestion;
import com.sb.solutions.core.enums.Status;

@Service
public class EligibilityCriteriaServiceImpl implements EligibilityCriteriaService {

    private final Logger logger = LoggerFactory.getLogger(EligibilityCriteriaServiceImpl.class);

    private final EligibilityCriteriaRepository eligibilityCriteriaRepository;

    public EligibilityCriteriaServiceImpl(
        EligibilityCriteriaRepository eligibilityCriteriaRepository) {
        this.eligibilityCriteriaRepository = eligibilityCriteriaRepository;
    }

    @Override
    public List<EligibilityCriteria> findAll() {
        logger.debug("Retrieving all eligibility criteria.");
        return eligibilityCriteriaRepository.findAll();
    }

    @Override
    public EligibilityCriteria findOne(Long id) {
        logger.debug("Retrieving the eligibility criteria with id [{}].", id);
        return eligibilityCriteriaRepository.getOne(id);
    }

    @Override
    public EligibilityCriteria save(EligibilityCriteria eligibilityCriteria) {
        logger.debug("Saving the eligibility criteria in database.");
        eligibilityCriteria.getQuestions()
            .forEach(eligibilityQuestion -> eligibilityQuestion
                .setEligibilityCriteria(eligibilityCriteria));
        return eligibilityCriteriaRepository.save(eligibilityCriteria);
    }

    @Override
    public Page<EligibilityCriteria> findAllPageable(Object t, Pageable pageable) {
        logger.debug("Retrieving a page of eligibility criteria.");
        return eligibilityCriteriaRepository.findAll(pageable);
    }

    @Override
    public List<EligibilityCriteria> saveAll(List<EligibilityCriteria> list) {
        return eligibilityCriteriaRepository.saveAll(list);
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting the eligibility criteria.");
        final EligibilityCriteria eligibilityCriteria = eligibilityCriteriaRepository.getOne(id);
        eligibilityCriteria.setStatus(Status.DELETED);
        eligibilityCriteriaRepository.save(eligibilityCriteria);
    }

    @Override
    public EligibilityCriteria update(EligibilityCriteria eligibilityCriteria, Long id) {
        logger.debug("Updating the eligiblity criteria.");
        EligibilityCriteria savedEligibilityCriteria = eligibilityCriteriaRepository.getOne(id);
        List<EligibilityQuestion> deletedEligibilityQuestions =
            savedEligibilityCriteria.getQuestions().stream()
                .filter(eligibilityQuestion -> eligibilityCriteria.getQuestions().stream()
                    .noneMatch(modifiedQuestions -> eligibilityQuestion.getId()
                        .equals(modifiedQuestions.getId())))
                .collect(Collectors.toList());
        deletedEligibilityQuestions
            .forEach(eligibilityQuestion -> eligibilityQuestion.setStatus(Status.DELETED));
        eligibilityCriteria.getQuestions().addAll(deletedEligibilityQuestions);
        eligibilityCriteria.getQuestions().forEach(
            eligibilityQuestion -> eligibilityQuestion.setEligibilityCriteria(eligibilityCriteria));
        return eligibilityCriteriaRepository.save(eligibilityCriteria);
    }

    @Override
    public EligibilityCriteria getByStatus(Status status) {
        logger.debug("Retrieving the active eligibility criteria.");
        return eligibilityCriteriaRepository
            .findByStatus(Status.ACTIVE);
    }
}
