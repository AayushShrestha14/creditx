package com.sb.solutions.api.eligibility.applicant.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.eligibility.answer.entity.EligibilityAnswer;
import com.sb.solutions.api.eligibility.answer.service.AnswerService;
import com.sb.solutions.api.eligibility.applicant.entity.Applicant;
import com.sb.solutions.api.eligibility.applicant.repository.ApplicantRepository;
import com.sb.solutions.api.eligibility.applicant.repository.specification.ApplicantSpecificationBuilder;
import com.sb.solutions.api.eligibility.applicant.service.ApplicantService;
import com.sb.solutions.api.eligibility.common.EligibilityConstants;
import com.sb.solutions.api.eligibility.common.EligibilityStatus;
import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.api.eligibility.criteria.service.EligibilityCriteriaService;
import com.sb.solutions.api.eligibility.document.dto.DocumentDTO;
import com.sb.solutions.api.eligibility.document.entity.SubmissionDocument;
import com.sb.solutions.api.eligibility.document.service.SubmissionDocumentService;
import com.sb.solutions.api.eligibility.utility.EligibilityUtility;
import com.sb.solutions.api.filestorage.service.FileStorageService;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.ArithmeticExpressionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final Logger logger = LoggerFactory.getLogger(ApplicantServiceImpl.class);

    private final ApplicantRepository applicantRepository;

    private final FileStorageService fileStorageService;

    private final SubmissionDocumentService submissionDocumentService;

    private final UserService userService;

    private final AnswerService answerService;

    private final EligibilityCriteriaService eligibilityCriteriaService;

    private final LoanConfigService loanConfigService;

    public ApplicantServiceImpl(ApplicantRepository applicantRepository,
        @Autowired UserService userService,
        FileStorageService fileStorageService,
        SubmissionDocumentService submissionDocumentService, AnswerService answerService,
        EligibilityCriteriaService eligibilityCriteriaService,
        LoanConfigService loanConfigService) {
        this.applicantRepository = applicantRepository;
        this.fileStorageService = fileStorageService;
        this.submissionDocumentService = submissionDocumentService;
        this.userService = userService;
        this.answerService = answerService;
        this.eligibilityCriteriaService = eligibilityCriteriaService;
        this.loanConfigService = loanConfigService;
    }

    @Override
    public List<Applicant> findAll() {
        logger.debug("Retrieving list of all the applicants.");
        return applicantRepository.findAll();
    }

    @Override
    public Applicant findOne(Long id) {
        logger.debug("Retrieving applicant with the id [{}].", id);
        return applicantRepository.getOne(id);
    }

    @Override
    public Applicant save(Applicant applicant) {
        logger.debug("Saving the applicant information.");
        applicant.setObtainedMarks(
            applicant.getAnswers().stream().map(Answer::getPoints).mapToLong(Long::valueOf).sum());
        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant save(Applicant applicant, Long loanConfigId) {
        logger.debug("Saving the applicant information.");
        final EligibilityCriteria eligibilityCriteria = eligibilityCriteriaService
            .getByStatus(Status.ACTIVE);
        if (eligibilityCriteria == null) {
            // this block will be executed if there is no any questions
            return applicantRepository.save(applicant);
        }
        LoanConfig currentLoanConfig = loanConfigService.findOne(loanConfigId);

        String formula = eligibilityCriteria.getFormula();

        final double interestRate = currentLoanConfig.getInterestRate();
        final String interestRateCharacter = "I";
        final String tenureCharacter = "T";
        Double tenureValue = null;

        // Mapping Operand Characters to their respective questions via QuestionId
        Map<String, String> operands = EligibilityUtility
            .extractOperands(formula, eligibilityCriteria.getQuestions());

        // Replacing mapped operand character with applicant answers for respective questions
        for (Map.Entry<String, String> operand : operands.entrySet()) {
            for (EligibilityAnswer eligibilityAnswer : applicant.getEligibilityAnswers()) {
                if (String.valueOf(eligibilityAnswer.getEligibilityQuestion().getId())
                    .equals(operand.getValue())) {
                    formula = formula
                        .replace(operand.getKey(), String.valueOf(eligibilityAnswer.getValue()));
                }
                if (tenureCharacter.equalsIgnoreCase(operand.getKey())) {
                    tenureValue = eligibilityAnswer.getValue();
                }
            }
            if (interestRateCharacter.equalsIgnoreCase(operand.getKey())) {
                formula = formula.replace(operand.getKey(), String.valueOf(interestRate));
            }
        }

        // Extracting remaining amount via mapped formula
        double remainingAmount = ArithmeticExpressionUtils
            .parseExpression(formula);

        // Saving eligibility Answers and Obtained Points..
        List<Answer> answers =
            answerService.findByIds(
                applicant.getAnswers().stream().map(Answer::getId).collect(Collectors.toList()));
        applicant.setObtainedMarks(
            answers.stream().map(Answer::getPoints).mapToLong(Long::valueOf).sum());
        applicant.getEligibilityAnswers()
            .forEach(eligibilityAnswer -> eligibilityAnswer.setApplicant(applicant));

        if (remainingAmount <= 0) {
            applicant.setEligibilityStatus(EligibilityStatus.NOT_ELIGIBLE);
            return applicantRepository.save(applicant);
        }
        double eligibleAmount =
            remainingAmount * eligibilityCriteria.getPercentageOfAmount() / 100D;
        if (eligibleAmount < currentLoanConfig.getMinimumProposedAmount()) {
            applicant.setEligibilityStatus(EligibilityStatus.NOT_ELIGIBLE);
            return applicantRepository.save(applicant);
        }

        // Calculating EMI Amount--
        String emiFormula = "L*(I/1200.0)*(((1.0+(I/1200.0))^(T*12.0))/(((1.0+(I/1200.0))^(T*12.0))-1))";
        emiFormula = emiFormula.replace("I", String.valueOf(interestRate))
            .replace("L", String.valueOf(eligibleAmount))
            .replace("T", String.valueOf(tenureValue == null ? 10 : tenureValue));

        final double emiAmount = ArithmeticExpressionUtils.parseExpression(emiFormula);

        applicant.setEmiAmount(emiAmount);
        applicant.setEligibleAmount(eligibleAmount);
        applicant.setEligibilityStatus(EligibilityStatus.ELIGIBLE);

        return applicantRepository.save(applicant);
    }

    @Override
    public Applicant update(Applicant applicant) {
        Applicant updateApplicant = applicantRepository.getOne(applicant.getId());
        updateApplicant.setEligibilityStatus(applicant.getEligibilityStatus());
        return applicantRepository.save(updateApplicant);
    }

    @Override
    public Page<Applicant> findAllPageable(Object t, Pageable pageable) {
        logger.debug("Retrieving a page of applicant list.");
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        /*String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("branchIds", branchAccess);*/
        final ApplicantSpecificationBuilder applicantSpecificationBuilder = new ApplicantSpecificationBuilder(
            s);
        final Specification<Applicant> specification = applicantSpecificationBuilder.build();
        return applicantRepository.findAll(specification, pageable);
    }

    @Override
    public List<Applicant> saveAll(List<Applicant> list) {
        return applicantRepository.saveAll(list);
    }

    @Override
    public Applicant saveDocuments(List<DocumentDTO> documents, long applicantId) {
        logger.debug("Saving documents of the applicant.");
        Applicant applicant = applicantRepository.getOne(applicantId);
        List<SubmissionDocument> submittedDocuments = new ArrayList<>();
        for (DocumentDTO document : documents) {
            String destinationPath = fileStorageService
                .getFilePath(EligibilityConstants.ELIGIBILITY_DIRECTORY,
                    EligibilityConstants.LOAN_CONFIG_DIRECTORY,
                    String.valueOf(applicant.getLoanConfig().getId()),
                    EligibilityConstants.APPLICANT_DIRECTORY,
                    String.valueOf(applicant.getId()));
            String imageUrl =
                "/applicant-documents/" + fileStorageService.storeDocument(document.getImage(),
                    document.getName(), destinationPath);
            submittedDocuments.add(getSubmissionDocument(document, imageUrl));
        }
        applicant.setDocuments(submittedDocuments);
        applicant = applicantRepository.save(applicant);
        return applicant;
    }

    private SubmissionDocument getSubmissionDocument(DocumentDTO document, String imageUrl) {
        SubmissionDocument submissionDocument = new SubmissionDocument();
        submissionDocument.setName(document.getName());
        submissionDocument.setType(document.getType());
        submissionDocument.setUrl(imageUrl);
        submissionDocument = submissionDocumentService.save(submissionDocument);
        return submissionDocument;
    }
}
