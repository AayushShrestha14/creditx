package com.sb.solutions.web.accountOpening.v1;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.openingForm.entity.OpeningCustomer;
import com.sb.solutions.api.openingForm.entity.OpeningForm;
import com.sb.solutions.api.openingForm.service.OpeningFormService;
import com.sb.solutions.core.constant.EmailConstant.Template;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.AccountStatus;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.date.DateManipulator;
import com.sb.solutions.core.utils.email.Email;
import com.sb.solutions.core.utils.email.MailThreadService;
import com.sb.solutions.core.utils.file.ByteToMultipartFile;
import com.sb.solutions.core.utils.file.FileUploadUtils;

@RestController
@RequestMapping("/v1/accountOpening")
public class AccountOpeningController {

    private final OpeningFormService openingFormService;
    private final MailThreadService mailThreadService;
    private final Logger logger = LoggerFactory.getLogger(AccountOpeningController.class);
    final ObjectMapper mapper = new ObjectMapper();
    @Value("${bank.name}")
    private String bankName;
    @Value("${bank.website}")
    private String bankWebsite;
    @Value("${bank.affiliateId}")
    private String affiliateId;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public AccountOpeningController(
        @Autowired OpeningFormService openingFormService,
        @Autowired MailThreadService mailThreadService
    ) {
        this.openingFormService = openingFormService;
        this.mailThreadService = mailThreadService;
    }

    @PostMapping
    public ResponseEntity<?> saveCustomer(@Valid @RequestBody OpeningForm openingForm) {
        OpeningForm c = openingFormService.save(openingForm);
        if (c == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {

            // notify user and branch user for new request
            Email email = new Email();
            email.setBankName(this.bankName);
            email.setBankWebsite(this.bankWebsite);
            email.setAffiliateId(this.affiliateId);
            email.setBankBranch(c.getBranch().getName());
            // get branch user's email
            List<String> representativesEmailIds = openingFormService
                .getUsersEmailHavingAccountOpeningPermissionInBranch(c.getBranch().getId());
            for (OpeningCustomer customer : c.getOpeningAccount().getOpeningCustomers()) {
                String customerName = customer.getFirstName() + ' ' + customer.getLastName();
                email.setTo(customer.getEmail());
                email.setToName(customerName);
                mailThreadService.sendMain(Template.ACCOUNT_OPENING_THANK_YOU, email);
                if (null != representativesEmailIds && representativesEmailIds.size() > 0) {
                    email.setBody(customerName);
                    email.setToName("Account Opening Representative");
                    representativesEmailIds.parallelStream().forEach(id -> {
                        email.setTo(id);
                        mailThreadService
                            .sendMain(Template.ACCOUNT_OPENING_BRANCH_NOTIFICATION, email);
                    });
                }
            }

            logger.debug("Email sent for Account Opening Request");
            return new RestResponseDto().successModel(c);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new RestResponseDto().successModel(openingFormService.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id,
        @RequestBody OpeningForm openingForm) {
        OpeningForm oldOpeningForm = openingFormService.findOne(id);
        AccountStatus previousStatus = oldOpeningForm.getStatus();
        OpeningForm newOpeningForm = openingFormService.save(openingForm);
        if (newOpeningForm == null) {
            return new RestResponseDto().failureModel("Couldn't update customer");
        } else {
            if (!previousStatus.equals(newOpeningForm.getStatus())) {
                Email email = new Email();
                email.setBankName(this.bankName);
                email.setBankWebsite(this.bankWebsite);
                email.setAffiliateId(this.affiliateId);
                email.setBankBranch(newOpeningForm.getBranch().getName());
                email.setAccountType(newOpeningForm.getAccountType().getName());
                for (OpeningCustomer customer : newOpeningForm.getOpeningAccount()
                    .getOpeningCustomers()) {
                    email.setTo(customer.getEmail());
                    email.setToName(customer.getFirstName() + ' ' + customer.getLastName());
                    if (newOpeningForm.getStatus().equals(AccountStatus.APPROVAL)) {
                        mailThreadService.sendMain(Template.ACCOUNT_OPENING_ACCEPT, email);
                    }
                }
            }
            return new RestResponseDto().successModel(newOpeningForm);
        }
    }

    @PostMapping(value = "/action")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody OpeningActionDto openingActionDto) {
        OpeningForm openingForm = openingFormService.findOne(openingActionDto.getId());
        openingForm.setStatus(openingActionDto.getActionStatus());
        OpeningForm savedAccountOpening = openingFormService.save(openingForm);
        if (ObjectUtils.isEmpty(savedAccountOpening)) {
            return new RestResponseDto()
                .failureModel("Error while " + openingActionDto.getActionStatus() + "ing" + "form");
        }
        openingActionDto.getOpeningCustomers().forEach(openingCustomer -> {
            Email email = new Email();
            email.setBankName(this.bankName);
            email.setBankWebsite(this.bankWebsite);
            email.setBankBranch(openingForm.getBranch().getName());
            email.setAccountType(openingForm.getAccountType().getName());
            email.setAccountNumber(savedAccountOpening.getAccountNumber());
            email.setTo(openingCustomer.getEmail());
            email.setAffiliateId(this.affiliateId);
            email.setName(openingCustomer.getFirstName() + ' ' + openingCustomer.getLastName());
            mailThreadService.sendMain(Template.ACCOUNT_OPENING_ACCEPT, email);
        });
        return new RestResponseDto().successModel(savedAccountOpening);
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> getStatus() {
        return new RestResponseDto().successModel(openingFormService.getStatus());
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(openingFormService.findAll());
    }

    @PostMapping(value = "/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchObject,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            openingFormService.findAllPageable(searchObject, PaginationUtils.pageable(page, size)));
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> saveUserFile(@RequestBody UploadDto uploadDto
    ) {
        String originalFilename = uploadDto.getOriginalFilename();
        byte[] bytes = uploadDto.getMultipartFile();
        ByteToMultipartFile multipartFile = new ByteToMultipartFile(bytes, originalFilename);

        return FileUploadUtils.uploadAccountOpeningFile(multipartFile,
            uploadDto.getBranch(), uploadDto.getType(), uploadDto.getName(),
            uploadDto.getCitizenship(), uploadDto.getCustomerName());
    }

    // requested whole form for future use
    @PatchMapping("/update-form-data/{id}")
    public ResponseEntity<?> updateFormData(@PathVariable Long id,
        @RequestBody Object requestData) {
        Map<String, Object> d = objectMapper.convertValue(requestData, Map.class);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OpeningForm openingForm = openingFormService.findOne(id);
        d.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(OpeningForm.class, k);
            if (!ObjectUtils.isEmpty(field)) {
                field.setAccessible(true);
                if (DateManipulator.isValidDate(v)) {
                    ReflectionUtils.setField(field, openingForm, new Date(v.toString()));
                } else {
                    ReflectionUtils.setField(field, openingForm,
                        objectMapper.convertValue(v, field.getType()));
                }
            }
        });
        return new RestResponseDto().successModel(openingFormService.save(openingForm));
    }

}
