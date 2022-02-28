package com.sb.solutions.api.openingForm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.branch.service.BranchService;
import com.sb.solutions.api.openingForm.entity.OpeningAccount;
import com.sb.solutions.api.openingForm.entity.OpeningCustomer;
import com.sb.solutions.api.openingForm.entity.OpeningCustomerRelative;
import com.sb.solutions.api.openingForm.entity.OpeningForm;
import com.sb.solutions.api.openingForm.repository.OpeningFormRepository;
import com.sb.solutions.api.openingForm.repository.specification.OpeningFormSpecBuilder;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.AccountStatus;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.date.DateValidation;
import com.sb.solutions.core.utils.jsonConverter.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpeningFormServiceImpl implements OpeningFormService {

    private final Logger logger = LoggerFactory.getLogger(OpeningFormServiceImpl.class);
    private OpeningFormRepository openingFormRepository;
    private BranchService branchService;
    private UserService userService;
    private JsonConverter jsonConverter = new JsonConverter();

    @Autowired
    public OpeningFormServiceImpl(
        OpeningFormRepository openingFormRepository,
        BranchService branchService,
        UserService userService) {
        this.openingFormRepository = openingFormRepository;
        this.branchService = branchService;
        this.userService = userService;
    }

    @Override
    public List<OpeningForm> findAll() {
        return openingFormRepository.findAll();
    }

    @Override
    public OpeningForm findOne(Long id) {
        OpeningForm openingForm = openingFormRepository.findById(id).get();
        openingForm.setOpeningAccount(jsonConverter
            .convertToJson(FilePath.getOSPath() + openingForm.getCustomerDetailsJson(),
                OpeningAccount.class));
        return openingForm;
    }

    @Override
    public OpeningForm save(OpeningForm openingForm) {
        openingForm.setBranch(branchService.findOne(openingForm.getBranch().getId()));
        validateDateFields(openingForm);
        if (openingForm.getId() == null) {
            openingForm.setRequestedDate(new Date());
            openingForm.setStatus(AccountStatus.NEW_REQUEST);
            try {
                String url = new PathBuilder(UploadDir.initialDocument)
                    .withBranch(openingForm.getBranch().getName())
                    .withCustomerName(openingForm.getFullName())
                    .withCitizenship(
                        openingForm.getOpeningAccount().getOpeningCustomers().iterator().next()
                            .getCitizenNumber())
                    .isJsonPath(true)
                    .buildAccountOpening();
                openingForm.setCustomerDetailsJson(writeJsonFile(url, openingForm));
            } catch (Exception exception) {
                throw new ServiceValidationException("File Fail to Save");
            }

        } else {
            try {
                String url = openingForm.getCustomerDetailsJson();
                openingForm.setCustomerDetailsJson(updateJsonFile(url, openingForm));
            } catch (Exception exception) {
                throw new ServiceValidationException("File Fail to Save");
            }
        }

        return openingFormRepository.save(openingForm);
    }

    @Override
    public Page<OpeningForm> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        String currentUserBranches = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        s.put("branch", s.getOrDefault("branch", currentUserBranches));
        OpeningFormSpecBuilder builder = new OpeningFormSpecBuilder(s);
        Specification<OpeningForm> specification = builder.build();
        return openingFormRepository.findAll(specification, pageable);
    }

    @Override
    public List<OpeningForm> saveAll(List<OpeningForm> list) {
        return openingFormRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> getStatus() {
        List<Long> currentUserBranches = userService.getRoleAccessFilterByBranch();
        return openingFormRepository.openingFormStatusCount(currentUserBranches);
    }

    @Override
    public List<String> getUsersEmailHavingAccountOpeningPermissionInBranch(long branchId) {
         return openingFormRepository.getUsersEmailHavingAccountOpeningPermissionInBranch(
            AppConstant.ACCOUNT_OPENING_PERMISSION, branchId);
    }

    private void validateDateFields(OpeningForm openingForm) {
        // Nominee
        if (openingForm.getOpeningAccount().getNominee().getDateOfBirth() != null && !DateValidation
            .isPreviousDate(openingForm.getOpeningAccount().getNominee().getDateOfBirth())) {
            throw new ServiceValidationException("Invalid Date of Birth of Nominee");
        }
        // Beneficiary
        if (openingForm.getOpeningAccount().getBeneficiary().getDateOfBirth() != null
            && !DateValidation
            .isPreviousDate(openingForm.getOpeningAccount().getBeneficiary().getDateOfBirth())) {
            throw new ServiceValidationException("Invalid Date of Birth of Beneficiary");
        }
        // Applicant
        for (OpeningCustomer openingCustomer : openingForm.getOpeningAccount()
            .getOpeningCustomers()) {
            if (openingCustomer.getDateOfBirthAD() != null && !DateValidation
                .isPreviousDate(openingCustomer.getDateOfBirthAD())) {
                throw new ServiceValidationException("Invalid Date of Birth of Customer");
            }
            if (openingCustomer.getCitizenIssuedDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getCitizenIssuedDate())) {
                throw new ServiceValidationException("Invalid Citizen Issued Date of Customer");
            }
            if (openingCustomer.getPassportIssuedDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getPassportIssuedDate())) {
                throw new ServiceValidationException(
                    "Invalid Password Issued Date of Customer");
            }
            if (openingCustomer.getPassportExpireDate() != null && !DateValidation
                .isFutureDate(openingCustomer.getPassportExpireDate())) {
                throw new ServiceValidationException(
                    "Invalid Passport Expire Date of Customer");
            }
            if (openingCustomer.getIdCardIssuedDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getIdCardIssuedDate())) {
                throw new ServiceValidationException("Invalid Id Issued Date of Customer");
            }
            if (openingCustomer.getIdCardExpireDate() != null && !DateValidation
                .isFutureDate(openingCustomer.getIdCardExpireDate())) {
                throw new ServiceValidationException("Invalid Id Expire Date of Customer");
            }
            if (openingCustomer.getVisaIssueDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getVisaIssueDate())) {
                throw new ServiceValidationException("Invalid Visa Issued Date of Customer");
            }
            if (openingCustomer.getVisaValidity() != null && !DateValidation
                .isFutureDate(openingCustomer.getVisaValidity())) {
                throw new ServiceValidationException("Invalid Visa Validity Date of Customer");
            }
            if (openingCustomer.getVoterIssuedDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getVoterIssuedDate())) {
                throw new ServiceValidationException("Invalid Voter Issued Date");
            }
            if (openingCustomer.getLicenseIssuedDate() != null && !DateValidation
                .isPreviousDate(openingCustomer.getLicenseIssuedDate())) {
                throw new ServiceValidationException("Invalid Driving License Issued Date");
            }
            if (openingCustomer.getLicenseExpireDate() != null && !DateValidation
                .isFutureDate(openingCustomer.getLicenseExpireDate())) {
                throw new ServiceValidationException("Invalid Driving License Expiry Date");
            }
            for (OpeningCustomerRelative openingCustomerRelative : openingCustomer.getKyc()
                .getCustomerRelatives()) {
                if (openingCustomerRelative.getCitizenshipIssuedDate() != null && !DateValidation
                    .isPreviousDate(openingCustomerRelative.getCitizenshipIssuedDate())) {
                    throw new ServiceValidationException(
                        "Invalid Citizen Issued Date of Customer Relative");
                }
            }
        }
    }

    private String writeJsonFile(String url, OpeningForm openingForm) {
        String jsonPath;
        Path path = Paths.get(FilePath.getOSPath() + url);
        if (!Files.exists(path)) {
            new File(FilePath.getOSPath() + url).mkdirs();
        }
        jsonPath = url + System.currentTimeMillis() + ".json";
        File file = new File(FilePath.getOSPath() + jsonPath);
        file.getParentFile().mkdirs();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            logger.error("Error occurred while writing the file {}", e.getMessage());
        }
        try {
            writer.write(jsonConverter.convertToJson(openingForm.getOpeningAccount()));
        } catch (IOException e) {
            logger.error("Error occurred while writing the file {}", e.getMessage());
        }
        try {
            writer.flush();
            return jsonPath;
        } catch (IOException e) {
            logger.error("Error occurred while flushing file writer {}", e.getMessage());
        }
        return null;
    }

    private String updateJsonFile(String url, OpeningForm openingForm) {
        String jsonPath = url;
        try {
            FileWriter writer = new FileWriter(FilePath.getOSPath() + url);
            try {
                writer.write(jsonConverter.convertToJson(openingForm.getOpeningAccount()));
            } catch (IOException e) {
                logger.error("Error occurred while writing the file {}", e.getMessage());
            }
            try {
                writer.flush();
                return jsonPath;
            } catch (IOException e) {
                logger.error("Error occurred while flushing file writer {}", e.getMessage());
            }
        } catch (Exception e) {
            String uploadUrl = new PathBuilder(UploadDir.initialDocument)
                .withBranch(openingForm.getBranch().getName())
                .withCustomerName(openingForm.getFullName())
                .withCitizenship(
                    openingForm.getOpeningAccount().getOpeningCustomers().iterator().next()
                        .getCitizenNumber())
                .isJsonPath(true)
                .buildAccountOpening();
            jsonPath = writeJsonFile(uploadUrl, openingForm);
            return jsonPath;
        }
        return null;
    }
}
