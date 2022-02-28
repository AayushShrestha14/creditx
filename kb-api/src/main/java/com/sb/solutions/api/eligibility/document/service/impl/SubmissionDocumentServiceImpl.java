package com.sb.solutions.api.eligibility.document.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.eligibility.document.entity.SubmissionDocument;
import com.sb.solutions.api.eligibility.document.repository.SubmissionDocumentRepository;
import com.sb.solutions.api.eligibility.document.service.SubmissionDocumentService;

@Service
public class SubmissionDocumentServiceImpl implements SubmissionDocumentService {

    private final Logger logger = LoggerFactory.getLogger(SubmissionDocumentServiceImpl.class);

    private final SubmissionDocumentRepository submissionDocumentRepository;

    public SubmissionDocumentServiceImpl(
        SubmissionDocumentRepository submissionDocumentRepository) {
        this.submissionDocumentRepository = submissionDocumentRepository;
    }

    @Override
    public List<SubmissionDocument> findAll() {
        logger.debug("Retrieving all submission documents.");
        return submissionDocumentRepository.findAll();
    }

    @Override
    public SubmissionDocument findOne(Long id) {
        logger.debug("Retrieving submission document with id [{}].", id);
        return submissionDocumentRepository.getOne(id);
    }

    @Override
    public SubmissionDocument save(SubmissionDocument submissionDocument) {
        logger.debug("Saving the submission document [{}].", submissionDocument);
        return submissionDocumentRepository
            .save(submissionDocument);
    }

    @Override
    public Page<SubmissionDocument> findAllPageable(Object t, Pageable pageable) {
        logger.debug("Retrieving submission documents.");
        return submissionDocumentRepository.findAll(pageable);
    }

    @Override
    public List<SubmissionDocument> saveAll(List<SubmissionDocument> list) {
        return submissionDocumentRepository.saveAll(list);
    }
}
