package com.sb.solutions.web.eligibility.v1.applicant;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.branch.service.BranchService;
import com.sb.solutions.api.eligibility.applicant.entity.Applicant;
import com.sb.solutions.api.eligibility.applicant.service.ApplicantService;
import com.sb.solutions.api.eligibility.common.EligibilityStatus;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.core.constant.EmailConstant.Template;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.email.Email;
import com.sb.solutions.core.utils.email.MailSenderService;
import com.sb.solutions.web.eligibility.v1.applicant.dto.ApplicantDto;
import com.sb.solutions.web.eligibility.v1.applicant.mapper.ApplicantMapper;

@RestController
@RequestMapping(ApplicantController.URL)
public class ApplicantController {

    static final String URL = "/v1/loan-configs/{loanConfigId}/applicants";
    private final Logger logger = LoggerFactory.getLogger(ApplicantController.class);
    private final ApplicantService applicantService;
    private final ApplicantMapper applicantMapper;
    private final MailSenderService mailSenderService;
    private final BranchService branchService;
    private final LoanConfigService loanConfigService;

    @Value("${bank.name}")
    private String bankName;
    @Value("${bank.website}")
    private String bankWebsite;

    @Value("${bank.affiliateId}")
    private String affiliateId;

    @Autowired
    public ApplicantController(
        ApplicantService applicantService,
        ApplicantMapper applicantMapper,
        MailSenderService mailSenderService,
        BranchService branchService,
        LoanConfigService loanConfigService
    ) {
        this.applicantService = applicantService;
        this.applicantMapper = applicantMapper;
        this.mailSenderService = mailSenderService;
        this.branchService = branchService;
        this.loanConfigService = loanConfigService;
    }

    @SuppressWarnings("checkstyle:LineLength")
    @PostMapping
    public final ResponseEntity<?> saveApplicant(@Valid @RequestBody Applicant applicant,
        @PathVariable long loanConfigId) {
        logger.debug("Rest request to save the applicant information.");

        applicant.setEligibilityStatus(EligibilityStatus.NEW_REQUEST);
        final Applicant savedApplicant = applicantService.save(applicant, loanConfigId);
        if (savedApplicant == null) {
            return new RestResponseDto().failureModel("Oops! Something went wrong.");
        } else {
            sendMail(savedApplicant);
            return new RestResponseDto()
                .successModel(applicantMapper.mapEntityToDto(savedApplicant));
        }
    }

    @PostMapping("/update")
    public final ResponseEntity<?> updateApplicant(@RequestBody ApplicantDto applicantDto) {
        Applicant applicant = applicantService.update(applicantMapper.mapDtoToEntity(applicantDto));
        if (applicant.getEligibilityStatus() == EligibilityStatus.APPROVED) {
            sendMail(applicant);
        }
        return new RestResponseDto().successModel(applicantMapper.mapEntityToDto(applicant));
    }

    private void sendMail(Applicant applicant) {
        Email email = new Email();
        Branch branch = branchService.findOne(applicant.getBranch().getId());
        email.setBankName(this.bankName);
        email.setBankWebsite(this.bankWebsite);
        email.setBankBranch(branch.getName());
        email.setTo(applicant.getEmail());
        email.setToName(applicant.getFullName());
        email.setPhoneNumber(branch.getLandlineNumber());
        email.setAffiliateId(this.affiliateId);
        email.setLoanType(
            loanConfigService.findOne(applicant.getLoanConfig().getId()).getName());
        if (applicant.getEligibilityStatus() == EligibilityStatus.ELIGIBLE
            || applicant.getEligibilityStatus() == EligibilityStatus.NOT_ELIGIBLE
            || applicant.getEligibilityStatus() == EligibilityStatus.NEW_REQUEST) {
            mailSenderService.send(Template.ELIGIBILITY_REQUEST, email);
        } else if (applicant.getEligibilityStatus() == EligibilityStatus.APPROVED) {
            mailSenderService.send(Template.ELIGIBILITY_APPROVE, email);
        }
        logger.debug("Email sent for Customer Eligibility {}", applicant);
    }

}
