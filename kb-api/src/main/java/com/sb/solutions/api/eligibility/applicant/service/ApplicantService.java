package com.sb.solutions.api.eligibility.applicant.service;

import java.util.List;

import com.sb.solutions.api.eligibility.applicant.entity.Applicant;
import com.sb.solutions.api.eligibility.document.dto.DocumentDTO;
import com.sb.solutions.core.service.BaseService;

public interface ApplicantService extends BaseService<Applicant> {

    Applicant saveDocuments(List<DocumentDTO> documents, long applicantId);

    Applicant save(Applicant applicant, Long loanConfigId);

    Applicant update(Applicant applicant);
}
