package com.sb.solutions.web.loan.v1.mapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.api.approvallimit.service.ApprovalLimitService;
import com.sb.solutions.api.collateralSiteVisit.dto.CollateralSiteVisitDto;
import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import com.sb.solutions.api.collateralSiteVisit.service.CollateralSiteVisitService;
import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.service.CustomerGeneralDocumentService;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.StatisticDto;
import com.sb.solutions.api.loan.dto.CustomerLoanDto;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.common.stage.mapper.StageMapper;
import com.sb.solutions.web.customerInfo.v1.dto.CustomerTransferDTO;
import com.sb.solutions.web.loan.v1.dto.BarChartDto;
import com.sb.solutions.web.loan.v1.dto.SeriesDto;
import com.sb.solutions.web.user.dto.UserDto;
import org.apache.maven.shared.utils.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.util.*;

/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Component
public class Mapper {

    private static final Logger logger = LoggerFactory.getLogger(Mapper.class);
    private final StageMapper stageMapper;
    private final ApprovalLimitService approvalLimitService;
    private final UserService userService;
    private final CollateralSiteVisitService collateralSiteVisitService;
    private final CustomerLoanService customerLoanService;
    Gson gson = new Gson();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));
    private final CustomerGeneralDocumentService customerGeneralDocumentService;

    public Mapper(@Autowired StageMapper stageMapper,
                  @Autowired ApprovalLimitService approvalLimitService,
                  UserService userService,
                  CollateralSiteVisitService collateralSiteVisitService,
                  CustomerLoanService customerLoanService,
                  CustomerGeneralDocumentService customerGeneralDocumentService) {
        this.stageMapper = stageMapper;
        this.approvalLimitService = approvalLimitService;
        this.userService = userService;
        this.collateralSiteVisitService = collateralSiteVisitService;
        this.customerLoanService = customerLoanService;
        this.customerGeneralDocumentService = customerGeneralDocumentService;
    }

    public CustomerLoan actionMapper(StageDto loanActionDto, CustomerLoan customerLoan,
                                     User currentUser) {
        if ((loanActionDto.getDocAction() != DocAction.PULLED) && (loanActionDto
                .getDocAction() != DocAction.TRANSFER) && (loanActionDto.getDocAction()
                != DocAction.RE_INITIATE)) {
            if (!customerLoan.getCurrentStage().getToUser().getId().equals(currentUser.getId())) {
                throw new ServiceValidationException("Sorry this document is not under you!!");
            }
        }
        if (loanActionDto.getDocAction() == DocAction.RE_INITIATE
                && customerLoan.getDocumentStatus() != DocStatus.REJECTED) {
            throw new ServiceValidationException(
                    "Re-initiate failed: Document status should be REJECTED!");
        }
        if (loanActionDto.getDocAction() == DocAction.APPROVED ||
                loanActionDto.getDocAction() == DocAction.CLOSED ||
                loanActionDto.getDocAction() == DocAction.REJECT) {
            customerLoan = checkDocument(loanActionDto, customerLoan, currentUser);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        List previousList = customerLoan.getPreviousList();
        List previousListTemp = new ArrayList();
        LoanStage loanStage = new LoanStage();
        if (customerLoan.getCurrentStage() != null) {
            loanStage = customerLoan.getCurrentStage();
            Map<String, String> tempLoanStage = objectMapper
                    .convertValue(customerLoan.getCurrentStage(), Map.class);
            try {
                previousList.forEach(p -> {
                    try {
                        Map<String, String> previous = objectMapper.convertValue(p, Map.class);

                        previousListTemp.add(objectMapper.writeValueAsString(previous));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to handle JSON data");
                    }
                });
                String jsonValue = objectMapper.writeValueAsString(tempLoanStage);
                previousListTemp.add(jsonValue);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to Get Stage data");
            }
        }
        if (loanActionDto.getDocAction() == DocAction.FORWARD
                || loanActionDto.getDocAction() == DocAction.BACKWARD) {
            if (customerLoan.getDocumentStatus() == DocStatus.UNDER_REVIEW || customerLoan
                    .getDocumentStatus() == DocStatus.PENDING) {
                customerLoan.setDocumentStatus(DocStatus.PENDING);
            } else {
                throw new ServiceValidationException(
                        "Forward Failed:Document Status should be under Review or Pending!!");
            }
        }
        customerLoan.setPreviousStageList(previousListTemp.toString());
        customerLoan.setDocumentStatus(loanActionDto.getDocumentStatus());
        StageDto currentStage = objectMapper.convertValue(loanStage, StageDto.class);
        UserDto currentUserDto = objectMapper.convertValue(currentUser, UserDto.class);
        loanStage = this
                .loanStages(loanActionDto, previousList, customerLoan.getCreatedBy(), currentStage,
                        currentUserDto, customerLoan);
        customerLoan.setCurrentStage(loanStage);
        customerLoan.setPreviousList(previousListTemp);
        if (((loanActionDto.getDocAction() == DocAction.FORWARD) && currentUser.getRole()
                .getRoleType() == RoleType.MAKER) ||
                (loanActionDto.getDocAction() == DocAction.TRANSFER) &&
                        (currentUser.getRole().getRoleType() == RoleType.MAKER ||
                                currentUser.getRole().getRoleType() == RoleType.COMMITTEE ||
                                currentUser.getRole().getRoleType() == RoleType.ADMIN)) {
            if (loanActionDto.getIsSol()) {
                User user = new User();
                Preconditions.checkNotNull(loanActionDto.getSolUser(),
                        "Please Select Approval User for Loan " + customerLoan.getLoan().getName());
                user.setId(loanActionDto.getSolUser().getId());
                customerLoan.setIsSol(Boolean.TRUE);
                customerLoan.setSolUser(user);
            } else {
                if ((loanActionDto.getDocAction() == DocAction.FORWARD) && currentUser.getRole().getRoleType() == RoleType.MAKER) {
                    customerLoan.setIsSol(Boolean.FALSE);
                    customerLoan.setSolUser(null);
                }
            }
        }
        return customerLoan;
    }

    private LoanStage loanStages(StageDto stageDto, List previousList, Long createdBy,
                                 StageDto currentStage, UserDto currentUser, CustomerLoan customerLoan) {
        if (stageDto.getDocAction() == DocAction.NOTED) {
            customerLoan.setNotedBy(currentUser.getId());
        } else if (currentStage.getDocAction() == DocAction.CLOSED
                || currentStage.getDocAction() == DocAction.APPROVED) {
            logger.error("Error while performing the action");
            throw new ServiceValidationException(
                    "Cannot Perform the action. Document has been " + currentStage.getDocAction());
        }
        // TODO: Separate alert message for no user and disabled user
        if (stageDto.getDocAction() == DocAction.FORWARD) {
            if (stageDto.getToRole() == null || stageDto.getToUser() == null) {
                logger.error("Error while performing the action");
                throw new ServiceValidationException(
                        "There is no user created in the role or is  disabled. Please contact admin.");
            }
            // Check loan flags
            List<CustomerLoanFlag> loanFlags = customerLoan.getLoanHolder().getLoanFlags()
                    .stream()
                    .filter(f -> f.getCustomerLoanId() != null && f.getCustomerLoanId()
                            .equals(customerLoan.getId()))
                    .collect(Collectors.toList());
            if (!loanFlags.isEmpty()) {
                loanFlags.sort(Comparator.comparingInt(CustomerLoanFlag::getOrder));
                logger.error(loanFlags.get(0).getDescription());
                throw new RuntimeException(loanFlags.get(0).getDescription());
            }
        }
        return stageMapper
                .mapper(stageDto, previousList, LoanStage.class, createdBy, currentStage, currentUser,
                        customerLoan);
    }

    public List<BarChartDto> toBarchartDto(List<StatisticDto> statistics) {
        final List<BarChartDto> charts = new ArrayList<>();
        Map<String, List<StatisticDto>> mappedStats =
                statistics.stream().collect(Collectors.groupingBy(StatisticDto::getLoanType));
        for (Map.Entry<String, List<StatisticDto>> entry : mappedStats.entrySet()) {
            final BarChartDto barChart = new BarChartDto();
            barChart.setName(entry.getKey());
            entry.getValue().forEach(statisticDto -> {
                final SeriesDto series = new SeriesDto();
                series.setName(statisticDto.getStatus().toString());
                series.setValue(statisticDto.getTotalAmount());
                series.setFileCount(statisticDto.getFiles());
                barChart.getSeries().add(series);
            });
            charts.add(barChart);
        }
        return charts;
    }

    public CustomerLoan transferBranchMapper(CustomerTransferDTO transferDTO, CustomerLoan customerLoan,
                                             User currentUser) {
        if (currentUser.getRole().getRoleType().equals(RoleType.ADMIN) || currentUser.getUsername().equalsIgnoreCase("SPADMIN")) {
            if (transferDTO.getDocAction().equals(DocAction.TRANSFER)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                List previousList = customerLoan.getPreviousList();
                List previousListTemp = new ArrayList();
                LoanStage loanStage = new LoanStage();
                if (customerLoan.getCurrentStage() != null) {
                    loanStage = customerLoan.getCurrentStage();
                    Map<String, String> tempLoanStage = objectMapper
                            .convertValue(loanStage, Map.class);
                    try {
                        previousList.forEach(p -> {
                            try {
                                Map<String, String> previous = objectMapper
                                        .convertValue(p, Map.class);
                                previousListTemp.add(objectMapper.writeValueAsString(previous));
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException("Failed to handle JSON data");
                            }
                        });
                        String currentLoanJsonValue = objectMapper.writeValueAsString(tempLoanStage);
                        previousListTemp.add(currentLoanJsonValue);
                        loanStage.setFromUser(loanStage.getToUser());
                        loanStage.setFromRole(loanStage.getToRole());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to get stage data");
                    }
                }
                customerLoan.setPreviousStageList(previousListTemp.toString());
                customerLoan.setPreviousList(previousListTemp);
                User toUser = userService.findOne(transferDTO.getToUserId());
                if (customerLoan.getCurrentStage() == null) {
                    loanStage.setFromUser(toUser);
                    loanStage.setFromRole(toUser.getRole());
                }
                loanStage.setToUser(toUser);
                loanStage.setToRole(toUser.getRole());
                loanStage.setDocAction(transferDTO.getDocAction());
                loanStage.setComment("Transferred by " + currentUser.getRole().getRoleName() + ". " + transferDTO.getComment());
                objectMapper.convertValue(loanStage, LoanStage.class);
                customerLoan.setCurrentStage(loanStage);
                customerLoan.setCreatedBy(toUser.getId());  //can not be set
                return customerLoan;
            } else {
                throw new ServiceValidationException(
                        "Transfer Failed: Action other than branch transfer detected!!!");
            }
        } else {
            throw new ServiceValidationException(
                    "Transfer Failed: You are not authorized to transfer branch!!!");
        }
    }

    private String actionType(LoanType loanType) {
        switch (loanType) {
            case NEW_LOAN:
                return "NEW";
            case RENEWED_LOAN:
                return "RENEW";
            case CLOSURE_LOAN:
                return "CLOSE";
            case ENHANCED_LOAN:
                return "ENHANCE";
            case FULL_SETTLEMENT_LOAN:
                return "FULL_SETTLEMENT";
            case PARTIAL_SETTLEMENT_LOAN:
                return "PARTIAL_SETTLEMENT";
            default:
                return "";
        }
    }

    private CustomerLoan checkDocument(StageDto loanActionDto, CustomerLoan customerLoan, User currentUser) {
        if (loanActionDto.getDocAction() == DocAction.APPROVED) {
            if (customerLoan.getLoanType() == LoanType.NEW_LOAN) {
                Map<String, Object> proposalData = gson.fromJson(customerLoan.getProposal().getData(), HashMap.class);
                proposalData.replace("existingLimit", 0);
                customerLoan.getProposal().setData(gson.toJson(proposalData));
            } else {
                Map<String, Object> proposalData = gson.fromJson(customerLoan.getProposal().getData(), HashMap.class);
                customerLoan.getProposal().setData(gson.toJson(proposalData));
            }

            // Save all existing loans of customer to approved.json file
            List<CustomerLoan> customerLoanList = customerLoanService.getAllLoansByLoanHolderId(customerLoan.getLoanHolder().getId());
            List<CustomerLoanDto> customerLoanDtoList = new ArrayList<>();
            customerLoanList.forEach(cl -> {
                if (!cl.getId().equals(customerLoan.getId()) && cl.getChildId() == null) {
                    CustomerLoanDto customerLoanDto = new CustomerLoanDto();
                    customerLoanDto.setLoanHolderId(String.valueOf(cl.getLoanHolder().getId()));
                    customerLoanDto.setCustomerLoanId(String.valueOf(cl.getId()));
                    customerLoanDto.setProposedLimit(String.valueOf(cl.getProposal().getProposedLimit()));
                    customerLoanDto.setDocumentStatus(cl.getDocumentStatus());
                    customerLoanDto.setProposal(cl.getProposal());
                    customerLoanDto.setLoanType(cl.getLoanType());
                    customerLoanDto.setCreatedAt(cl.getCreatedAt());
                    customerLoanDto.setName(cl.getLoan().getName());
                    customerLoanDto.setLoanConfig(cl.getLoan());
                    customerLoanDtoList.add(customerLoanDto);
                }
            });
            customerLoan
                    .setCustomerLoanDtoList(
                            objectMapper
                                    .convertValue(customerLoanDtoList, List.class));
        }

        // Zip customer documents
        String basePath = new PathBuilder(UploadDir.initialDocument)
                .buildLoanDocumentUploadBasePathWithId(customerLoan.getLoanHolder().getId(),
                        customerLoan.getLoanHolder().getBranch().getId(),
                        customerLoan.getLoanHolder().getCustomerType().name(),
                        actionType(customerLoan
                                .getLoanType()),
                        customerLoan.getLoan().getId());
        String filePath = FilePath.getOSPath() + basePath;
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            new File(filePath).mkdirs();
        }
        String zipPath;
        String zipFolder;
        if (loanActionDto.getDocAction() == DocAction.APPROVED) {
            zipPath = basePath + customerLoan.getId() + "-approved-documents.zip";
            zipFolder = basePath + customerLoan.getId() + "-approved-documents";
        } else if (loanActionDto.getDocAction() == DocAction.CLOSED) {
            zipPath = basePath + customerLoan.getId() + "-closed-documents.zip";
            zipFolder = basePath + customerLoan.getId() + "-closed-documents";
        } else {
            zipPath = basePath + customerLoan.getId() + "-rejected-documents.zip";
            zipFolder = basePath + customerLoan.getId() + "-rejected-documents";
        }
        List<String> zipDocPaths = new ArrayList<>();
        List<String> siteVisitDocPath = new ArrayList<>();
        // Add general documents to zip file
        List<CustomerGeneralDocument> generalDocuments = customerGeneralDocumentService
                .findByCustomerInfoId(customerLoan.getLoanHolder().getId());
        if (generalDocuments != null) {
            generalDocuments.forEach(gen -> {
                if (gen.getDocPath() != null) {
                    zipDocPaths.add(FilePath.getOSPath() + gen.getDocPath());
                }
            });
        }
        // Add insurance documents to zip file
        List<Insurance> insurance = customerLoan.getLoanHolder().getInsurance();
        if (insurance != null) {
            insurance.forEach(ins -> {
                if (ins.getPolicyDocumentPath() != null) {
                    String[] docPathArray = ins.getPolicyDocumentPath().split(",");
                    for (String singlePath: docPathArray) {
                        zipDocPaths.add(FilePath.getOSPath() + singlePath);
                    }
                }
            });
        }
        // Add guarantor documents to zip file
        if (!ObjectUtils.isEmpty(customerLoan.getTaggedGuarantors())) {
            Set <Guarantor> taggedGuarantors = customerLoan.getTaggedGuarantors();
            taggedGuarantors.forEach(tg -> {
                if (!ObjectUtils.isEmpty(tg.getDocPath())) {
                    String[] docPathArray = tg.getDocPath().split(",");
                    for(String singlePath: docPathArray) {
                        zipDocPaths.add(FilePath.getOSPath() + singlePath);
                    }
                }
            });
        }
        // Add collateral site visit documents to zip file
        if (customerLoan.getLoanHolder().getSecurity() != null) {
            List<CollateralSiteVisit> collateralSiteVisitList = collateralSiteVisitService.
                    getCollateralSiteVisitBySecurityId(customerLoan.getLoanHolder().getSecurity().getId());
            collateralSiteVisitList.forEach(collateralSiteVisit -> {
                if (collateralSiteVisit.getSiteVisitDocuments() != null) {
                    List<SiteVisitDocument> siteVisitDocumentList = collateralSiteVisit.getSiteVisitDocuments();
                    siteVisitDocumentList.forEach(siteVisitDocument -> {
                        zipDocPaths.add(FilePath.getOSPath() + siteVisitDocument.getDocPath() + siteVisitDocument.getDocName() + ".jpg");
                    });
                }
            });
        }
        try {
            if (!zipDocPaths.isEmpty()) {
                try {
                    File directory = new File(FilePath.getOSPath() + zipFolder);
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                    for (String doc : zipDocPaths) {
                        File src = new File(doc);
                        String fileName = src.getName();
                        String [] fullName = fileName.split("\\.");
                        String targetName = "";
                        for(int count = 0; count < fullName.length; count++) {
                            if (count == fullName.length - 1) {
                                targetName = targetName.replaceAll("\\.+$", "") + "_" + new Date().getTime() + "." + fullName[count];
                            } else {
                                targetName = targetName + fullName[count] + ".";
                            }
                        }
                        File target = new File(FilePath.getOSPath() + zipFolder + '\\' + targetName);
                        Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                    FileUploadUtils.createZip(FilePath.getOSPath() + zipFolder, FilePath.getOSPath() + zipPath);
                } catch (Exception ex) {
                    logger.error("Unable to zip file ", ex);
                } finally {
                    FileUtils.deleteDirectory(FilePath.getOSPath() + zipFolder);
                }
                customerLoan.setZipPath(zipPath);
            }
        } catch (Exception ex2) {
            logger.error("Unable to delete directory ", ex2);
        }
        if (ProductUtils.LAS && loanActionDto.getDocAction() == DocAction.APPROVED) {
            ApprovalLimit approvalLimit = approvalLimitService
                    .getByRoleAndLoan(currentUser.getRole().getId(),
                            customerLoan.getLoan().getId(), customerLoan.getLoanCategory());
            if (approvalLimit != null) {
                if (Double.valueOf(approvalLimit.getAmount().toString()) < Double
                        .valueOf(customerLoan.getProposal().getProposedLimit().toString())) {
                    throw new ServiceValidationException("Authority Limit Exceed");
                }
            } else {
                throw new ServiceValidationException("Authority Limit Is not set yet for this loan");
            }
            if (customerLoan.getDmsLoanFile() != null) {
                if (customerLoan.getDmsLoanFile().getProposedAmount().compareTo(approvalLimit
                        .getAmount()) == 1) {
                    // proposed amount is greater than approval limit
                    throw new RuntimeException("Amount Exceed");
                }
            }
        }
        if (loanActionDto.isNotify()) {
            customerLoan.setNotify(true);
        }
        if (customerLoan.getIsSol() && loanActionDto.getDocAction() == DocAction.APPROVED) {
            Preconditions.checkArgument(
                    customerLoan.getSolUser().getId() == currentUser.getId(),
                    "You don't have permission to Approve this file!!");
        }
        if (!ObjectUtils.isEmpty(customerLoan.getSecurity()) && !ObjectUtils.isEmpty(customerLoan.getSecurity().getId())) {
            try {
                List<CollateralSiteVisit> collateralSiteVisits = collateralSiteVisitService.getCollateralSiteVisitBySecurityId(customerLoan.getLoanHolder().getSecurity().getId());
                if (!ObjectUtils.isEmpty(collateralSiteVisits)) {
                    List<CollateralSiteVisitDto> collateralSiteVisitList = new ArrayList<>();
                    ZoneId defaultZoneId = ZoneId.systemDefault();
                    collateralSiteVisits.forEach(cs -> {
                        CollateralSiteVisitDto collateralSiteVisitDto = new CollateralSiteVisitDto();
                        collateralSiteVisitDto.setSecurityName(String.valueOf(cs.getSecurityName()));
                        objectMapper.registerModule(new JavaTimeModule());
                        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                        collateralSiteVisitDto.setSiteVisitDate(Date.from(cs.getSiteVisitDate().atStartOfDay(defaultZoneId).toInstant()));
                        collateralSiteVisitDto.setSiteVisitJsonData(cs.getSiteVisitJsonData());
                        List<SiteVisitDocument> siteVisitDocuments = cs.getSiteVisitDocuments();
                        if (siteVisitDocuments.size() > 0) {
                            for (SiteVisitDocument siteVisitDocument : siteVisitDocuments) {
                                siteVisitDocument.setIsApproved(true);
                            }
                        }
                        collateralSiteVisitDto.setSiteVisitDocuments(siteVisitDocuments);
                        collateralSiteVisitList.add(collateralSiteVisitDto);
                    });
                    customerLoan.setCollateralSiteVisits(collateralSiteVisitList);
                }
            } catch (Exception e1) {
                logger.error("collateral site visit is empty");
            }
        }
        return customerLoan;
    }
}
