package com.sb.solutions.api.loan.service;

import static com.sb.solutions.core.constant.AppConstant.SEPERATOR_BLANK;
import static com.sb.solutions.core.constant.AppConstant.SEPERATOR_FRONT_SLASH;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.collateralSiteVisit.service.CollateralSiteVisitService;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.branch.service.BranchService;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.companyInfo.model.service.CompanyInfoService;
import com.sb.solutions.api.creditRiskGrading.service.CreditRiskGradingService;
import com.sb.solutions.api.creditRiskGradingAlpha.service.CreditRiskGradingAlphaService;
import com.sb.solutions.api.creditRiskGradingLambda.service.CreditRiskGradingLambdaService;
import com.sb.solutions.api.crg.service.CrgGammaService;
import com.sb.solutions.api.crgMicro.service.CrgMicroService;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.ClientType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customer.service.CustomerGeneralDocumentService;
import com.sb.solutions.api.customer.service.CustomerInfoService;
import com.sb.solutions.api.customer.service.CustomerService;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.aop.ActivityService;
import com.sb.solutions.api.customerActivity.entity.CustomerActivity;
import com.sb.solutions.api.customerActivity.enums.ActivityType;
import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.customerRelative.entity.CustomerRelative;
import com.sb.solutions.api.dms.dmsloanfile.service.DmsLoanFileService;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.financial.service.FinancialService;
import com.sb.solutions.api.group.service.GroupServices;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperIdType;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.insurance.service.InsuranceService;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.PieChartDto;
import com.sb.solutions.api.loan.StatisticDto;
import com.sb.solutions.api.loan.dto.CustomerInfoLoanDto;
import com.sb.solutions.api.loan.dto.CustomerLoanCsvDto;
import com.sb.solutions.api.loan.dto.CustomerLoanDto;
import com.sb.solutions.api.loan.dto.CustomerLoanFilterDto;
import com.sb.solutions.api.loan.dto.CustomerLoanGroupDto;
import com.sb.solutions.api.loan.dto.CustomerOfferLetterDto;
import com.sb.solutions.api.loan.dto.GroupDto;
import com.sb.solutions.api.loan.dto.GroupSummaryDto;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loan.entity.CadDocument;
import com.sb.solutions.api.loan.entity.CustomerDocument;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.entity.CustomerOfferLetter;
import com.sb.solutions.api.loan.mapper.NepaliTemplateMapper;
import com.sb.solutions.api.loan.repository.CustomerLoanRepository;
import com.sb.solutions.api.loan.repository.CustomerLoanRepositoryJdbcTemplate;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpecBuilder;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.api.nepalitemplate.entity.NepaliTemplate;
import com.sb.solutions.api.nepalitemplate.service.NepaliTemplateService;
import com.sb.solutions.api.preference.notificationMaster.service.NotificationMasterService;
import com.sb.solutions.api.proposal.entity.Proposal;
import com.sb.solutions.api.proposal.service.ProposalService;
import com.sb.solutions.api.security.service.SecurityService;
import com.sb.solutions.api.sharesecurity.ShareSecurity;
import com.sb.solutions.api.sharesecurity.service.ShareSecurityService;
import com.sb.solutions.api.siteVisit.service.SiteVisitService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.api.vehiclesecurity.service.VehicleSecurityService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.DocumentCheckType;
import com.sb.solutions.core.enums.LoanFlag;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.repository.customCriteria.BaseCriteriaQuery;
import com.sb.solutions.core.repository.customCriteria.dto.CriteriaDto;
import com.sb.solutions.core.utils.BankUtils;
import com.sb.solutions.core.utils.FilterJsonUtils;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.core.utils.string.StringUtil;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.ReportType;
import com.sb.solutions.report.core.factory.ReportFactory;
import com.sb.solutions.report.core.model.Report;


/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Service
public class CustomerLoanServiceImpl implements CustomerLoanService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoanServiceImpl.class);
    private static final String DESCRIPTION_APPROVED = "%s has been approved Successfully";
    private static final String DESCRIPTION_REJECTED = "%s has been rejected successfully";
    private static final String DESCRIPTION_CLOSED = "%s has been closed successfully";
    private final CustomerLoanRepository customerLoanRepository;
    private final UserService userService;
    private final CustomerService customerService;
    private final DmsLoanFileService dmsLoanFileService;
    private final CompanyInfoService companyInfoService;
    private final SiteVisitService siteVisitService;
    private final FinancialService financialService;
    private final CreditRiskGradingAlphaService creditRiskGradingAlphaService;
    private final CrgMicroService crgMicroService;
    private final CreditRiskGradingLambdaService creditRiskGradingLambdaService;
    private final SecurityService securityService;
    private final ProposalService proposalService;
    private final GroupServices groupService;
    private final CustomerOfferService customerOfferService;
    private final CreditRiskGradingService creditRiskGradingService;
    private final CrgGammaService crgGammaService;
    private final VehicleSecurityService vehicleSecurityService;
    private final ShareSecurityService shareSecurityService;
    private final NepaliTemplateService nepaliTemplateService;
    private final NepaliTemplateMapper nepaliTemplateMapper;
    private final InsuranceService insuranceService;
    private final NotificationMasterService notificationMasterService;
    private final CustomerLoanFlagService customerLoanFlagService;
    private final CustomerInfoService customerInfoService;
    private final ActivityService activityService;
    private final CustomerLoanRepositoryJdbcTemplate customerLoanRepositoryJdbcTemplate;
    private final CustomerGeneralDocumentService customerGeneralDocumentService;
    private final CadDocumentService cadDocumentService;
    private final LoanConfigService loanConfigService;
    private long countCustomer, countBranch;
    private BigDecimal totalLoan;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));
    private final BranchService branchService;
    private final CustomerDocumentService customerDocumentService;
    private final CollateralSiteVisitService collateralSiteVisitService;

    @Autowired
    EntityManager em;
    Gson gson = new Gson();
    private List customerGroupLogList = new ArrayList();


    public CustomerLoanServiceImpl(
        CustomerLoanRepository customerLoanRepository,
        UserService userService,
        CustomerService customerService,
        CompanyInfoService companyInfoService,
        DmsLoanFileService dmsLoanFileService,
        SiteVisitService siteVisitService,
        FinancialService financialService,
        CreditRiskGradingAlphaService creditRiskGradingAlphaService,
        CrgMicroService crgMicroService,
        CreditRiskGradingLambdaService creditRiskGradingLambdaService,
        SecurityService securityservice,
        ProposalService proposalService,
        CustomerOfferService customerOfferService,
        CreditRiskGradingService creditRiskGradingService,
        CrgGammaService crgGammaService,
        GroupServices groupService,
        VehicleSecurityService vehicleSecurityService,
        ShareSecurityService shareSecurityService,
        NepaliTemplateService nepaliTemplateService,
        NepaliTemplateMapper nepaliTemplateMapper,
        InsuranceService insuranceService,
        NotificationMasterService notificationMasterService,
        CustomerLoanFlagService customerLoanFlagService,
        CustomerInfoService customerInfoService,
        ActivityService activityService,
        CustomerLoanRepositoryJdbcTemplate customerLoanRepositoryJdbcTemplate,
        CustomerGeneralDocumentService customerGeneralDocumentService,
        CadDocumentService cadDocumentService,
        LoanConfigService loanConfigService,
        BranchService branchService,
        CustomerDocumentService customerDocumentService,
        CollateralSiteVisitService collateralSiteVisitService) {
        this.customerLoanRepository = customerLoanRepository;
        this.userService = userService;
        this.customerService = customerService;
        this.companyInfoService = companyInfoService;
        this.dmsLoanFileService = dmsLoanFileService;
        this.siteVisitService = siteVisitService;
        this.financialService = financialService;
        this.creditRiskGradingAlphaService = creditRiskGradingAlphaService;
        this.crgMicroService = crgMicroService;
        this.creditRiskGradingLambdaService = creditRiskGradingLambdaService;
        this.securityService = securityservice;
        this.proposalService = proposalService;
        this.customerOfferService = customerOfferService;
        this.creditRiskGradingService = creditRiskGradingService;
        this.crgGammaService = crgGammaService;
        this.groupService = groupService;
        this.vehicleSecurityService = vehicleSecurityService;
        this.shareSecurityService = shareSecurityService;
        this.nepaliTemplateService = nepaliTemplateService;
        this.nepaliTemplateMapper = nepaliTemplateMapper;
        this.insuranceService = insuranceService;
        this.notificationMasterService = notificationMasterService;
        this.customerLoanFlagService = customerLoanFlagService;
        this.customerInfoService = customerInfoService;
        this.activityService = activityService;
        this.customerLoanRepositoryJdbcTemplate = customerLoanRepositoryJdbcTemplate;
        this.customerGeneralDocumentService = customerGeneralDocumentService;
        this.cadDocumentService = cadDocumentService;
        this.branchService = branchService;
        this.loanConfigService = loanConfigService;
        this.customerDocumentService = customerDocumentService;
        this.collateralSiteVisitService = collateralSiteVisitService;
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    @Override
    public List<CustomerLoan> findAll() {
        return customerLoanRepository.findAll();
    }

    @Override
    public CustomerLoan findOne(Long id) {
        CustomerLoan customerLoan = customerLoanRepository.findById(id).get();
        String data = customerLoan.getData();
        if (ProductUtils.NEP_TEMPLATE) {
            Map<String, String> filterParams = new HashMap<>();
            filterParams.put("customerLoan.id", String.valueOf(id));
            List<NepaliTemplate> nepaliTemplates = nepaliTemplateService
                .findAllBySpec(filterParams);
            if (!nepaliTemplates.isEmpty()) {
                customerLoan
                    .setNepaliTemplates(nepaliTemplateMapper.mapEntitiesToDtos(nepaliTemplates));
            }
        }
        if (!ObjectUtils.isEmpty(customerLoan.getLoanHolder().getCustomerGroup())) {
            customerLoan.setGroupSummaryDto(
                getLoanByCustomerGroup(customerLoan.getLoanHolder().getCustomerGroup()));
        }
        if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED) ||
                customerLoan.getDocumentStatus().equals(DocStatus.REJECTED) ||
                customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
            customerLoan = readJsonFile(customerLoan, id);
        }
        if (ProductUtils.CAD_LITE_VERSION) {
            CustomerLoan customer1 = customerLoanRepository.findById(id).get();
            List<CadDocument> cadDocument = customer1.getCadDocument();
            if (!ObjectUtils.isEmpty(cadDocument)) {
                customerLoan.setCadDocument(cadDocument);
            }
        }
        if (ProductUtils.OFFER_LETTER) {
            CustomerOfferLetter customerOfferLetter = customerOfferService
                .findByCustomerLoanId(customerLoan.getId());
            if (customerOfferLetter != null) {
                CustomerOfferLetterDto customerOfferLetterDto = new CustomerOfferLetterDto();
                BeanUtils.copyProperties(customerOfferLetter, customerOfferLetterDto);
                customerOfferLetterDto.setId(customerOfferLetter.getId());
                customerLoan.setCustomerOfferLetter(customerOfferLetterDto);
            }
        }
        if (!customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)
                && !customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)
                && !customerLoan.getDocumentStatus().equals(DocStatus.REJECTED)) {
            List<CustomerGeneralDocument> generalDocuments = customerGeneralDocumentService
                    .findByCustomerInfoId(customerLoan.getLoanHolder().getId());
            if (!generalDocuments.isEmpty()) {
                customerLoan.getLoanHolder().setCustomerGeneralDocuments(generalDocuments);
            }
        }
        // skip insurace , pan expiry for loan forward and approve
        customerLoan.getLoanHolder().setLoanFlags(customerLoan.getLoanHolder().getLoanFlags()
            .stream().filter(lf -> lf.getFlag() != LoanFlag.INSURANCE_EXPIRY
                && lf.getFlag() != LoanFlag.COMPANY_VAT_PAN_EXPIRY
                && lf.getFlag() != LoanFlag.EMPTY_COMPANY_VAT_PAN_EXPIRY)
            .collect(Collectors.toList()));
        customerLoan.setData(data);
        return mapLoanHolderToCustomerLoan(customerLoan);
    }

    Boolean isLoanEditable(CustomerLoan customerLoan, User loggedInUser) {
        User processionUser = customerLoan.getCurrentStage().getToUser();
        Role processionRole = customerLoan.getCurrentStage().getToRole();
        return processionUser.getId().longValue() == loggedInUser.getId().longValue()
                && processionRole.getId().longValue() == loggedInUser.getRole().getId().longValue();
    }

    @Transactional
    @Override
    public CustomerLoan save(CustomerLoan customerLoan) {
        if (!userService.getAuthenticatedUser().getRole().getRoleType().equals(RoleType.MAKER)) {
            throw new ServiceValidationException("You don't have privilege to save loan");
        }
        // validation start
        if (customerLoan.getLoan() == null) {
            throw new ServiceValidationException("Loan can not be null");
        }
        CompanyInfo companyInfo = null;
        boolean isNewLoan = false;

        if (customerLoan.getId() == null) {
            isNewLoan = true;
            customerLoan.setBranch(userService.getAuthenticatedUser().getBranch().get(0));
            LoanStage stage = new LoanStage();
            stage.setToRole(userService.getAuthenticatedUser().getRole());
            stage.setFromRole(userService.getAuthenticatedUser().getRole());
            stage.setFromUser(userService.getAuthenticatedUser());
            stage.setToUser(userService.getAuthenticatedUser());
            stage.setComment(DocAction.DRAFT.name());
            stage.setDocAction(DocAction.DRAFT);
            customerLoan.setCurrentStage(stage);
        }

//        if (null != companyInfo) {
//            customerLoan
//                .setCompanyInfo(
//                    this.companyInfoService.findOne(customerLoan.getLoanHolder().getAssociateId()));

            /*
            if business loan , business pan/vat number will be citizenship number , companay name will be customer name
            and establishment date will be issue date
             */

//            customer.setCustomerName(companyInfo.getCompanyName());
//            customer.setCitizenshipNumber(companyInfo.getPanNumber());
//            customer.setCitizenshipIssuedDate(companyInfo.getEstablishmentDate());
//            customer.setOccupation(companyInfo.getBusinessType().toString());
//            // look whether customer already exists
//            try {
//                Customer existingCustomer = customerService
//                    .findCustomerByCustomerNameAndCitizenshipNumberAndCitizenshipIssuedDate(
//                        customer.getCustomerName(), customer.getCitizenshipNumber(),
//                        customer.getCitizenshipIssuedDate()
//                    );
//                if (existingCustomer != null) {
//
//                    customer.setId(existingCustomer.getId());
//                }
//            } catch (Exception e) {
//                logger.debug(" No customer {} with pan {} exists", customer.getCustomerName(),
//                    customer.getCitizenshipNumber());
//            }
//

//        }

//        if (customer != null) {
//            Customer c = this.customerService
//                .findOne(customerLoan.getLoanHolder().getAssociateId());
//            customerLoan.setCustomerInfo(c);
//            customerLoan
//                .setLoanHolder(customerInfoService.findByAssociateIdAndCustomerType(c.getId(),
//                    CustomerType.INDIVIDUAL));
//        }

        if (customerLoan.getFinancial() != null) {
            customerLoan.setFinancial(customerLoan.getLoanHolder().getFinancial());
        }
        if (customerLoan.getSecurity() != null) {
            customerLoan.setSecurity(customerLoan.getLoanHolder().getSecurity());
        }
        if (customerLoan.getSiteVisit() != null) {
            customerLoan.setSiteVisit(customerLoan.getLoanHolder().getSiteVisit());
        }
        if (customerLoan.getProposal() != null) {
            customerLoan.setProposal(this.proposalService.save(customerLoan.getProposal()));
        }
        if (customerLoan.getCreditRiskGrading() != null) {
            customerLoan.setCreditRiskGrading(
                customerLoan.getLoanHolder().getCreditRiskGrading());
        }
        if (customerLoan.getGroup() != null) {
            customerLoan.setGroup(this.groupService.save(customerLoan.getGroup()));
        }
        if (customerLoan.getVehicleSecurity() != null) {
            customerLoan
                .setVehicleSecurity(null);
        }

        if (customerLoan.getCreditRiskGradingAlpha() != null) {
            customerLoan.setCreditRiskGradingAlpha(
                this.creditRiskGradingAlphaService.save(customerLoan.getCreditRiskGradingAlpha()));
        }

        if (customerLoan.getCreditRiskGradingLambda() != null) {
            customerLoan.setCreditRiskGradingLambda(this.creditRiskGradingLambdaService
                .save(customerLoan.getCreditRiskGradingLambda()));
        }

        if (customerLoan.getCrgMicro() != null) {
            customerLoan.setCrgMicro(this.crgMicroService
                .save(customerLoan.getCrgMicro()));
        }

        if (customerLoan.getCrgGamma() != null) {
            customerLoan.setCrgGamma(this.crgGammaService.save(customerLoan.getCrgGamma()));
        }

        if (customerLoan.getShareSecurity() != null) {
            customerLoan
                .setShareSecurity(customerLoan.getLoanHolder().getShareSecurity());
        }
        if (customerLoan.getInsurance() != null) {
            customerLoan.setInsurance(customerLoan.getLoanHolder().getInsurance());
        }

        if (!isNewLoan) {
            CustomerLoan actualLoan = customerLoanRepository.findById(customerLoan.getId()).get();
            List<CustomerDocument> actualDocuments = actualLoan.getCustomerDocument();
            List<CustomerDocument> updatedDocuments = customerLoan.getCustomerDocument();

            for (CustomerDocument doc : actualDocuments) {
                Optional<CustomerDocument> optionalCustomerDocument = updatedDocuments.stream().filter(d -> Objects.equals(d.getId(), doc.getId())).findAny();
                if (!optionalCustomerDocument.isPresent()) {
                    customerDocumentService.deleteById(doc.getId());
                }
            }
        }

        CustomerLoan savedCustomerLoan = customerLoanRepository.save(customerLoan);
        postLoanConditionCheck(savedCustomerLoan);

        if (!customerLoan.getNepaliTemplates().isEmpty()) {
            List<NepaliTemplate> nepaliTemplates = nepaliTemplateMapper
                .mapDtosToEntities(customerLoan.getNepaliTemplates());
            nepaliTemplates.forEach(v -> v.setCustomerLoan(savedCustomerLoan));
            nepaliTemplateService.saveAll(nepaliTemplates);
        }

        if (isNewLoan) {
            List<CustomerDocument> customerDocuments = savedCustomerLoan.getCustomerDocument();
            customerDocuments.forEach(customerDocument -> {
                String docPath = customerDocument.getDocumentPath();
                String documentPath[] = docPath.split("tempDocument");
                documentPath[0] =
                    documentPath[0] + "loan-loan-" + savedCustomerLoan.getId() + "/" + "doc";
                String newDocPath = documentPath[0] + documentPath[1];
                customerDocument.setDocumentPath(newDocPath);
                FileUploadUtils.moveFile(docPath, newDocPath);
            });
            savedCustomerLoan.setCustomerDocument(customerDocuments);

            String refNumber = new StringBuilder().append(LocalDate.now().getYear())
                .append(LocalDate.now().getMonthValue())
                .append(LocalDate.now().getDayOfMonth()).append(SEPERATOR_FRONT_SLASH)
                .append(customerLoan.getLoan().getId()).append(SEPERATOR_FRONT_SLASH)
                .append(
                    StringUtil.getAcronym(customerLoan.getLoanCategory().name(), SEPERATOR_BLANK))
                .append(SEPERATOR_FRONT_SLASH).append(customerLoan.getId()).toString();
            customerLoanRepository.updateReferenceNo(refNumber, customerLoan.getId());
        }
        return savedCustomerLoan;
    }


    @Override
    public Page<CustomerLoan> findAllPageable(Object t, Pageable pageable) {

        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        User u = userService.getAuthenticatedUser();
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("currentUserRole", u.getRole() == null ? null : u.getRole().getId().toString());
        s.put("toUser", u == null ? null : u.getId().toString());
        s.put("branchIds", branchAccess);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        return customerLoanRepository.findAll(specification, pageable);
    }

    @Override
    public List<CustomerLoan> findAll(Object search) {

        Map<String, String> s = objectMapper.convertValue(search, Map.class);
        return customerLoanRepository
            .findALlUniqueLoanByCustomerId(Long.parseLong(s.get("loanHolderId")));
    }

    @Override
    public List<CustomerLoan> saveAll(List<CustomerLoan> list) {
        return customerLoanRepository.saveAll(list);
    }

    @Override
    public void sendForwardBackwardLoan(CustomerLoan customerLoan) {
        if (customerLoan.getCurrentStage() == null
            || customerLoan.getCurrentStage().getToRole() == null
            || customerLoan.getCurrentStage().getToUser() == null) {
            logger.warn("Empty current Stage{}", customerLoan.getCurrentStage());
            throw new ServiceValidationException("Unable to perform Task");
        }
        CustomerLoan customerLoan1 = customerLoanRepository.save(customerLoan);
        final User user = userService.findOne(customerLoan1.getModifiedBy());
        if (customerLoan1.getDocumentStatus().equals(DocStatus.APPROVED) ||
                customerLoan1.getDocumentStatus().equals(DocStatus.CLOSED) ||
                customerLoan1.getDocumentStatus().equals(DocStatus.REJECTED)) {
            customerLoan1 = createJsonFile(customerLoan1, user);
        }
        if (customerLoan1.getCurrentStage().getDocAction() == DocAction.RE_INITIATE) {
            try {
                CustomerActivity customerActivity = CustomerActivity.builder()
                    .customerLoanId(customerLoan1.getId())
                    .activityType(ActivityType.MANUAL)
                    .activity(Activity.RE_INITIATE_LOAN)
                    .modifiedOn(new Date())
                    .modifiedBy(user)
                    .profile(customerLoan1.getLoanHolder())
                    .data(objectMapper.writeValueAsString(customerLoan1))
                    .description(String
                        .format("Customer: %s loan facility: %s re-initiated by %s",
                            customerLoan1.getLoanHolder().getName(),
                            customerLoan1.getLoan().getName(), user.getName()))
                    .build();
                activityService.saveCustomerActivity(customerActivity);
            } catch (JsonProcessingException e) {
                throw new ServiceValidationException(
                    "Error: Unable to add customer activity!");
            }
        }
    }

    @Override
    public void sendForwardBackwardLoan(List<CustomerLoan> customerLoans) {
        if (customerLoans.get(0).getDocumentStatus().equals(DocStatus.APPROVED) && !ObjectUtils
            .isEmpty(customerLoans.get(0).getLoanHolder().getCustomerGroup())) {
            List<CustomerLoanDto> customerGroup = customerLoanRepositoryJdbcTemplate
                .findByLoanHolderCustomerGroupAndNotToCurrentLoanHolder(
                    customerLoans.get(0).getLoanHolder().getCustomerGroup().getId(),
                    customerLoans.get(0).getLoanHolder().getId());
            this.customerGroupLogList = customerGroup;
        }
        customerLoans.forEach(this::sendForwardBackwardLoan);
        this.customerGroupLogList = new ArrayList();
    }

    @Override
    public Map<String, Integer> statusCount() {
        User u = userService.getAuthenticatedUser();
        List<Long> branchAccess = userService.getRoleAccessFilterByBranch();
        if (ProductUtils.CUSTOMER_BASE_LOAN) {
            return customerLoanRepository
                .statusCountCustomerWise(u.getRole().getId(), branchAccess, u.getId());
        }
        return customerLoanRepository.statusCount(u.getRole().getId(), branchAccess, u.getId());
    }

    @Override
    public List<CustomerLoan> getFirst5CustomerLoanByDocumentStatus(DocStatus status) {
        User u = userService.getAuthenticatedUser();
        return customerLoanRepository
            .findFirst5ByDocumentStatusAndCurrentStageToRoleIdAndBranchIdOrderByIdDesc(status,
                u.getRole().getId(), u.getBranch().get(0).getId());
    }

    @Override
    public List<PieChartDto> proposedAmount(String startDate, String endDate)
        throws ParseException {
        List<Long> branchAccess = userService.getRoleAccessFilterByBranch();
        List<PieChartDto> data = new ArrayList<>();
        if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
            data = customerLoanRepository.proposedAmount(branchAccess);
        } else if (startDate == null || startDate.isEmpty()) {
            data = customerLoanRepository.proposedAmountBefore(branchAccess,
                new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
        } else if (endDate == null || endDate.isEmpty()) {
            data = customerLoanRepository.proposedAmountAfter(branchAccess, new SimpleDateFormat(
                "MM/dd/yyyy").parse(startDate));
        } else {
            data = customerLoanRepository.proposedAmountBetween(branchAccess, new SimpleDateFormat(
                "MM/dd/yyyy").parse(startDate), new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
        }
        return data;
    }

    @Override
    public List<PieChartDto> proposedAmountByBranch(Long branchId, String startDate,
        String endDate) throws ParseException {
        List<PieChartDto> data = new ArrayList<>();
        if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
            data = customerLoanRepository.proposedAmountByBranchId(branchId);
        } else if (startDate == null || startDate.isEmpty()) {
            data = customerLoanRepository.proposedAmountByBranchIdAndDateBefore(branchId,
                new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
        } else if (endDate == null || endDate.isEmpty()) {
            data = customerLoanRepository.proposedAmountByBranchIdAndDateAfter(branchId,
                new SimpleDateFormat(
                    "MM/dd/yyyy").parse(startDate));
        } else {
            data = customerLoanRepository.proposedAmountByBranchIdAndDateBetween(branchId,
                new SimpleDateFormat(
                    "MM/dd/yyyy").parse(startDate),
                new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
        }
        return data;
    }

    @Override
    public List<CustomerLoan> getByCitizenshipNumber(String citizenshipNumber) {
        return customerLoanRepository
            .getByCustomerInfoCitizenshipNumber(citizenshipNumber);
    }

    @Override
    public Page<CustomerLoan> getCatalogues(Object searchDto, Pageable pageable) {
        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("branchIds", branchAccess);
        s.values().removeIf(Objects::isNull);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        return customerLoanRepository.findAll(specification, pageable);
    }

    @Override
    public Page<CustomerLoan> getCommitteePull(Object searchDto, Pageable pageable) {
        User u = userService.getAuthenticatedUser();
        if (u.getRole().getRoleType().equals(RoleType.COMMITTEE)) {
            Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
            String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
            if (s.containsKey("branchIds")) {
                branchAccess = s.get("branchIds");
            }
            s.put("branchIds", branchAccess);
            s.put("currentUserRole", u.getRole().getId().toString());
            s.put("toUser",
                userService.findByRoleIdAndIsDefaultCommittee(u.getRole().getId(), true).get(0)
                    .getId()
                    .toString());
            s.values().removeIf(Objects::isNull);
            logger.info("query for pull {}", s);
            final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
            final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
            Page<CustomerLoan> customerLoanList = customerLoanRepository
                .findAll(specification, pageable);
            for (CustomerLoan c : customerLoanList.getContent()) {
                for (LoanStageDto l : c.getPreviousList()) {
                    if (l.getToUser().getId() == (u.getId())) {
                        c.setPulled(true);
                        break;
                    }
                }
            }
            return customerLoanList;
        }
        return null;
    }

    @Override
    public Page<CustomerLoan> getIssuedOfferLetter(Object searchDto, Pageable pageable) {
        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("branchIds", branchAccess);
        s.put("documentStatus", DocStatus.APPROVED.name());
        s.put("currentOfferLetterStage",
            String.valueOf(userService.getAuthenticatedUser().getId()));
        s.values().removeIf(Objects::isNull);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        return customerLoanRepository.findAll(specification, pageable);
    }

    @Override
    public CustomerLoan delCustomerLoan(Long id) {
        Optional<CustomerLoan> customerLoan = customerLoanRepository.findById(id);
        if (!customerLoan.isPresent()) {
            logger.info("No Loan Found of Id {}", id);
            throw new ServiceValidationException("No Loan Found of Id " + id);
        }
        if (!customerLoan.get().getCurrentStage().getDocAction().equals(DocAction.DRAFT)) {
            logger.info("Loan can not be deleted it is in stage", id);
            throw new ServiceValidationException("Loan can not be deleted it is in stage");
        }
        User u = userService.getAuthenticatedUser();
        if (u.getRole().getRoleType().equals(RoleType.MAKER)) {
            customerLoanRepository
                .deleteByIdAndCurrentStageDocAction(id, DocAction.DRAFT);

            if (!ObjectUtils.isEmpty(customerLoan.get().getParentId())) {
                //remove child Id from parent Id;
                customerLoanRepository
                    .updateCloseRenewChildId(null, customerLoan.get().getParentId(),
                        false);
            }

            if (!customerLoanRepository.findById(id).isPresent()) {
                return new CustomerLoan();
            } else {
                throw new ServiceValidationException("Oops Something went wrong");
            }
        } else {
            throw new ServiceValidationException("You don't have access to delete file");
        }
    }

    @Override
    public List<StatisticDto> getStats(Long branchId, String startDate, String endDate)
        throws ParseException {
        List<StatisticDto> statistics = new ArrayList<>();
        logger.debug("Request to get the statistics about the existing loans.");
        return getLasStatistics(branchId, startDate, endDate);
    }

    @Override
    public Map<String, String> chkUserContainCustomerLoan(Long id) {
        User u = userService.findOne(id);
        Integer count = 0;
        if (u.getRole().getRoleType().equals(RoleType.CAD_ADMIN) ||
                u.getRole().getRoleType().equals(RoleType.CAD_ADMIN) ||
                u.getRole().getRoleType().equals(RoleType.CAD_LEGAL) ||
                u.getRole().getRoleName().equalsIgnoreCase("CAD")) {
            count = customerLoanRepository.chkCadUserContainCustomerLoan(id);
        } else {
            List<DocStatus> statusList = new ArrayList<>();
            statusList.add(DocStatus.PENDING);
            statusList.add(DocStatus.UNDER_REVIEW);
            statusList.add(DocStatus.DOCUMENTATION);
            statusList.add(DocStatus.VALUATION);
            statusList.add(DocStatus.DISCUSSION);
            for (DocStatus s : statusList) {
                count += customerLoanRepository
                        .chkUserContainCustomerLoan(id, u.getRole().getId(), s);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("count", String.valueOf(count));
        map.put("status", count == 0 ? "false" : "true");
        return map;
    }


    private List<StatisticDto> getLasStatistics(Long branchId, String startDate, String endDate)
        throws ParseException {
        List<StatisticDto> data = new ArrayList<>();
        if (branchId == 0) {
            List<Long> branches = userService.getRoleAccessFilterByBranch();
            if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate
                .isEmpty())) {
                data = customerLoanRepository.getLasStatistics(branches);
            } else if (startDate == null || startDate.isEmpty()) {
                data = customerLoanRepository.getLasStatisticsAndDateBefore(branches,
                    new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
            } else if (endDate == null || endDate.isEmpty()) {
                data = customerLoanRepository
                    .getLasStatisticsAndDateAfter(branches, new SimpleDateFormat(
                        "MM/dd/yyyy").parse(startDate));
            } else {
                data = customerLoanRepository
                    .getLasStatisticsAndDateBetween(branches, new SimpleDateFormat(
                            "MM/dd/yyyy").parse(startDate),
                        new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
            }
        } else {
            if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate
                .isEmpty())) {
                data = customerLoanRepository.getLasStatisticsByBranchId(branchId);
            } else if (startDate == null || startDate.isEmpty()) {
                data = customerLoanRepository.getLasStatisticsByBranchIdAndDateBefore(branchId,
                    new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
            } else if (endDate == null || endDate.isEmpty()) {
                data = customerLoanRepository.getLasStatisticsByBranchIdAndDateAfter(branchId,
                    new SimpleDateFormat(
                        "MM/dd/yyyy").parse(startDate));
            } else {
                data = customerLoanRepository.getLasStatisticsByBranchIdAndDateBetween(branchId,
                    new SimpleDateFormat(
                        "MM/dd/yyyy").parse(startDate),
                    new SimpleDateFormat("MM/dd/yyyy").parse(endDate));
            }

        }
        return data;
    }

    @Override
    public CustomerLoan renewCloseEntity(CustomerLoan previousLoan) {
        final Long tempParentId = previousLoan.getId();
        previousLoan.setParentId(tempParentId);
        previousLoan.setId(null);
        previousLoan.setDocumentStatus(DocStatus.PENDING);

        if (previousLoan.getDmsLoanFile() != null) {
            previousLoan.getDmsLoanFile().setId(null);
            previousLoan.setDmsLoanFile(dmsLoanFileService.save(previousLoan.getDmsLoanFile()));
        }
        if (previousLoan.getFinancial() != null) {
            previousLoan.getFinancial().setId(null);
            previousLoan.setFinancial(financialService.save(previousLoan.getFinancial()));
        }
        if (previousLoan.getSecurity() != null) {
            previousLoan.getSecurity().setId(null);
            previousLoan.setSecurity(securityService.save(previousLoan.getSecurity()));
        }
        if (previousLoan.getSiteVisit() != null) {
            previousLoan.getSiteVisit().setId(null);
        }
        if (previousLoan.getProposal() != null) {
            previousLoan.getProposal().setId(null);
            previousLoan.setProposal(proposalService.save(previousLoan.getProposal()));
            if (previousLoan.getLoanType() != LoanType.NEW_LOAN) {
                Map<String , Object> proposalData = gson.fromJson(previousLoan.getProposal().getData() , HashMap.class);
                proposalData.replace("existingLimit" , previousLoan.getProposal().getProposedLimit());
                proposalData.replace("enhanceLimitAmount", 0);
                previousLoan.getProposal().setData(gson.toJson(proposalData));
                previousLoan.getProposal().setExistingLimit(previousLoan.getProposal().getProposedLimit());
                previousLoan.getProposal().setEnhanceLimitAmount(BigDecimal.ZERO);
            }
        }
        if (previousLoan.getCreditRiskGrading() != null) {
            previousLoan.getCreditRiskGrading().setId(null);
            previousLoan
                .setCreditRiskGrading(
                    creditRiskGradingService.save(previousLoan.getCreditRiskGrading()));
        }
        if (previousLoan.getGroup() != null) {
            previousLoan.getGroup().setId(null);
            previousLoan.setGroup(groupService.save(previousLoan.getGroup()));
        }
        if (previousLoan.getVehicleSecurity() != null) {
            previousLoan.getVehicleSecurity().setId(null);
            previousLoan
                .setVehicleSecurity(vehicleSecurityService.save(previousLoan.getVehicleSecurity()));
        }

        if (previousLoan.getCreditRiskGradingAlpha() != null) {
            previousLoan.getCreditRiskGradingAlpha().setId(null);
            previousLoan.setCreditRiskGradingAlpha(
                creditRiskGradingAlphaService.save(previousLoan.getCreditRiskGradingAlpha()));
        }
        if (previousLoan.getCreditRiskGradingLambda() != null) {
            previousLoan.getCreditRiskGradingLambda().setId(null);
            previousLoan.setCreditRiskGradingLambda(
                creditRiskGradingLambdaService.save(previousLoan.getCreditRiskGradingLambda()));
        }
        if (previousLoan.getCrgMicro() != null) {
            previousLoan.getCrgMicro().setId(null);
            previousLoan.setCrgMicro(
                crgMicroService.save(previousLoan.getCrgMicro()));
        }
        if (previousLoan.getCrgGamma() != null) {
            previousLoan.getCrgGamma().setId(null);
            previousLoan.setCrgGamma(
                crgGammaService.save(previousLoan.getCrgGamma()));
        }
        if (previousLoan.getShareSecurity() != null) {
            previousLoan.getShareSecurity().setId(null);
            previousLoan
                .setShareSecurity(this.shareSecurityService.save(previousLoan.getShareSecurity()));
        }
        if (previousLoan.getInsurance() != null) {
            previousLoan.getInsurance().forEach(value -> value.setId(null));
            previousLoan.setInsurance(this.insuranceService.saveAll(previousLoan.getInsurance()));
        }

        LoanStage stage = new LoanStage();
        stage.setToRole(userService.getAuthenticatedUser().getRole());
        stage.setFromRole(userService.getAuthenticatedUser().getRole());
        stage.setFromUser(userService.getAuthenticatedUser());
        stage.setToUser(userService.getAuthenticatedUser());
        stage.setComment(DocAction.DRAFT.name());
        stage.setDocAction(DocAction.DRAFT);
        previousLoan.setCurrentStage(stage);
        previousLoan.setPreviousList(null);
        previousLoan.setPreviousStageList(null);
        previousLoan.setCustomerDocument(null);
        CustomerLoan customerLoan = customerLoanRepository.save(previousLoan);
        customerLoanRepository
            .updateCloseRenewChildId(customerLoan.getId(), tempParentId, true);
        return customerLoan;
    }

    @Override
    public String csv(Object searchDto) {
        Report report = ReportFactory.getReport(populate(Optional.of(searchDto)));
        return getDownloadPath() + report.getFileName();

    }

    @Override
    public List<CustomerLoan> getLoanByCustomerId(Long id) {
        return customerLoanRepository.getCustomerLoanByCustomerInfoId(id);
    }

    @Override
    public List<CustomerLoan> getLoanByLoanHolderId(Long id) {
        return customerLoanRepository.getCustomerLoanByAndLoanHolderId(id);
    }

    @Override
    public List<CustomerLoan> getAllLoansByLoanHolderId(Long loanHolderId) {
        return customerLoanRepository.findAllByLoanHolderId(loanHolderId);
    }

    @Override
    public List<CustomerLoan> getFinalUniqueLoansById(Long id) {
        return customerLoanRepository.findCustomerLoansByLoanHolderId(id);
    }

    @Override
    public List<CustomerLoan> getLoanByCustomerKycGroup(CustomerRelative customerRelative) {
        String date = new SimpleDateFormat("yyyy-MM-dd")
            .format(customerRelative.getCitizenshipIssuedDate());
        Map<String, String> map = new HashMap<>();
        map.put("customerRelativeName", customerRelative.getCustomerRelativeName());
        map.put("citizenshipNumber", customerRelative.getCitizenshipNumber());
        map.put("citizenshipIssuedDate", date);
        map.values().removeIf(Objects::isNull);
        logger.info("get loan by kyc search parm{}", map);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(
            map);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        return customerLoanRepository.findAll(specification);

    }

    @Override
    public GroupSummaryDto getLoanByCustomerGroup(CustomerGroup customerGroup) {
        if (ObjectUtils.isEmpty(customerGroup.getId())) {
            throw new NullPointerException("group id cannot be null");
        }
        GroupSummaryDto groupSummaryDto = new GroupSummaryDto();
        AtomicReference<BigDecimal> grandTotalFundedAmount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> grandTotalNotFundedAmount = new AtomicReference<>(
            BigDecimal.ZERO);
        AtomicReference<BigDecimal> grandTotalApprovedLimit = new AtomicReference<>(
            BigDecimal.ZERO);
        AtomicReference<BigDecimal> grandTotalPendingLimit = new AtomicReference<>(BigDecimal.ZERO);
        // getting all loans in dto form
        List<CustomerLoanGroupDto> customerLoanGroupDtos = customerLoanRepository
            .getGroupDetailByCustomer(customerGroup.getId());

        Map<String, GroupDto> filterList = new HashMap<>();

        // iterate all loan dto to set unique as per customer
        customerLoanGroupDtos.stream().map(CustomerLoanGroupDto::getLoanHolderId).distinct()
            .forEach(id -> {
                GroupDto groupDto = new GroupDto();
                // getting current customer related loan dto
                List<CustomerLoanGroupDto> currentIdDtos = customerLoanGroupDtos.stream()
                    .filter(c -> Objects
                        .equals(c.getLoanHolderId(), id)
                        && c.getProposal() != null && c.getDocStatus().equals(DocStatus.APPROVED))
                    .collect(Collectors.toList());
                groupDto.setCustomerLoanGroupDto(currentIdDtos);

                // get funded , non funded data
                groupDto.setFundedData(currentIdDtos.stream().filter(s -> s.getLoanConfig()
                    .getIsFundable()).collect(Collectors.toList()));

                groupDto.setNonFundedData(currentIdDtos.stream().filter(s -> !s.getLoanConfig()
                    .getIsFundable()).collect(Collectors.toList()));

                // calculate funded , non funded data
                groupDto.setTotalFunded(calculateFundLimit(groupDto.getFundedData(), true));
                grandTotalFundedAmount.updateAndGet(v -> v.add(groupDto.getTotalFunded()));
                groupDto.setTotalNonFunded(calculateFundLimit(groupDto.getNonFundedData(), false));
                grandTotalNotFundedAmount.updateAndGet(v -> v.add(groupDto.getTotalNonFunded()));

                // calculate total approved limit
                BigDecimal totalApprovedLimit = calculateProposalLimit(
                    customerLoanGroupDtos.stream()
                        .filter(c -> c.getDocStatus() == DocStatus.APPROVED
                            && c.getLoanHolderId().equals(id))
                        .map(CustomerLoanGroupDto::getProposal).collect(Collectors.toList()));
                // calculate total pending limit
                BigDecimal totalPendingLimit = calculateProposalLimit(
                    customerLoanGroupDtos.stream()
                        .filter(c -> c.getDocStatus() != DocStatus.APPROVED &&
                            c.getLoanHolderId().equals(id)).map(CustomerLoanGroupDto::getProposal)
                        .collect(Collectors.toList()));

                groupDto.setTotalApprovedLimit(totalApprovedLimit);
                grandTotalApprovedLimit.updateAndGet(v -> v.add(groupDto.getTotalApprovedLimit()));
                groupDto.setTotalPendingLimit(totalPendingLimit);
                grandTotalPendingLimit.updateAndGet(v -> v.add(groupDto.getTotalPendingLimit()));

                filterList.put(id.toString(), groupDto);

            });
        groupSummaryDto.setGrandTotalFundedAmount(grandTotalFundedAmount.get());
        groupSummaryDto.setGrandTotalNotFundedAmount(grandTotalNotFundedAmount.get());
        groupSummaryDto.setGrandTotalPendingLimit(grandTotalPendingLimit.get());
        groupSummaryDto.setGrandTotalApprovedLimit(grandTotalApprovedLimit.get());
        groupSummaryDto.setGroupCode(customerGroup.getGroupCode());
        groupSummaryDto.setGroupId(customerGroup.getId());
        groupSummaryDto.setGroupLimit(customerGroup.getGroupLimit());
        groupSummaryDto.setGroupDtoList(new ArrayList<>(filterList.values()));

        return groupSummaryDto;

    }

    BigDecimal calculateFundLimit(List<CustomerLoanGroupDto> customerLoanGroupDtos,
        boolean isFunded) {
        return customerLoanGroupDtos.stream().filter(s -> s.getLoanConfig()
            .getIsFundable() == isFunded).map(s -> s.getProposal().getProposedLimit())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    BigDecimal calculateProposalLimit(List<Proposal> proposals) {
        return proposals.stream().map(Proposal::getProposedLimit)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Page<Customer> getCustomerFromCustomerLoan(Object searchDto, Pageable pageable) {

        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        s.put("distinctByCustomer", "true");
        s.values().removeIf(Objects::isNull);
        logger.info("search param for customer in customerLoan {}", s);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        Page<CustomerLoan> customerLoanPage = customerLoanRepository
            .findAll(specification, pageable);
        List<Customer> customerList = new ArrayList<>();
        customerLoanPage.getContent().forEach(customerLoan -> {
            if (!customerList.contains(customerLoan) && (customerLoan.getCustomerInfo() != null)) {
                customerList.add(customerLoan.getCustomerInfo());
            }
        });
        List<Customer> finalList = customerList.stream().filter(distinctByKey(Customer::getId))
            .collect(
                Collectors.toList());

        Page<Customer> pages = new PageImpl<Customer>(finalList, pageable,
            customerLoanPage.getTotalElements());
        return pages;
    }

    @Override
    public List<CustomerLoan> getLoanByCustomerGuarantor(Guarantor guarantor) {
        String date = new SimpleDateFormat("yyyy-MM-dd")
            .format(guarantor.getIssuedYear());
        Map<String, String> map = new HashMap<>();
        map.put("guarantorName", guarantor.getName());
        map.put("guarantorCitizenshipNumber", guarantor.getCitizenNumber());
        map.put("guarantorCitizenshipIssuedDate", date);
        map.put("guarantorDistrictId", String.valueOf(guarantor.getDistrict().getId()));
        map.put("guarantorProvinceId", String.valueOf(guarantor.getProvince().getId()));
        map.values().removeIf(Objects::isNull);
        logger.info("get loan by guarantor search parm{}", map);
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(
            map);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        return customerLoanRepository.findAll(specification);
    }

    public long calculateLoanSpanAndPossession(Date lastModifiedDate, Date createdLastDate) {
        int daysdiff = 0;
        Date lastModifiedAt = lastModifiedDate;
        Date createdLastAt = createdLastDate;

        long differenceInDate = lastModifiedAt.getTime() - createdLastAt.getTime();
        long diffInDays = TimeUnit.DAYS.convert(differenceInDate, TimeUnit.MILLISECONDS);
        daysdiff = (int) diffInDays;

        return daysdiff;

    }

    public long calculatePendingLoanSpanAndPossession(Date createdDate) {
        int daysDiff = 0;
        Date createdAt = createdDate;
        Date currentDate = new Date();

        long differenceInDate = currentDate.getTime() - createdAt.getTime();
        long diffInDays = TimeUnit.DAYS.convert(differenceInDate, TimeUnit.MILLISECONDS);
        daysDiff = (int) diffInDays;

        return daysDiff;

    }

    public String formatCsvDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Checks `proposed limit`, `share considered value` before saving loan.
     *
     * @param loan An instance of Customer Loan.
     */
    @Override
    public void postLoanConditionCheck(CustomerLoan loan) {
        // check if proposed amount is equal to ZERO
        if (loan.getProposal() != null) {
            CustomerLoanFlag customerLoanFlag = loan.getLoanHolder().getLoanFlags().stream()
                .filter(loanFlag -> (
                    loanFlag.getFlag().equals(LoanFlag.ZERO_PROPOSAL_AMOUNT)
                        && loanFlag.getCustomerLoanId().equals(loan.getId())
                )).collect(CustomerLoanFlag.toSingleton());

            boolean flag = loan.getProposal().getProposedLimit().compareTo(BigDecimal.ZERO) <= 0;
            if (flag && customerLoanFlag == null) {
                customerLoanFlag = new CustomerLoanFlag();
                customerLoanFlag.setCustomerInfo(loan.getLoanHolder());
                customerLoanFlag.setCustomerLoanId(loan.getId());
                customerLoanFlag.setFlag(LoanFlag.ZERO_PROPOSAL_AMOUNT);
                customerLoanFlag.setDescription(LoanFlag.ZERO_PROPOSAL_AMOUNT.getValue()[1]);
                customerLoanFlag
                    .setOrder(Integer.parseInt(LoanFlag.ZERO_PROPOSAL_AMOUNT.getValue()[0]));
                customerLoanFlagService.save(customerLoanFlag);
            } else if (!flag && customerLoanFlag != null) {
                customerLoanFlagService.deleteCustomerLoanFlagById(customerLoanFlag.getId());
            }
        }
        // check if company VAT/PAN registration will expire
        if (loan.getCompanyInfo() != null) {
            companyInfoService.execute(loan.getLoanHolder().getId());
        }

        if (!ObjectUtils.isEmpty(loan.getLoanHolder().getSecurity())) {
            HelperDto<Long> dto = new HelperDto<>(loan.getId(), HelperIdType.LOAN);
            securityService.execute(Optional.of(dto));
        }

        // check proposed limit VS considered amount
        ShareSecurity shareSecurity = loan.getLoanHolder().getShareSecurity();
        if (shareSecurity != null) {
            HelperDto<Long> dto = new HelperDto<>(loan.getId(), HelperIdType.LOAN);
            shareSecurityService.execute(Optional.of(dto));
        }
        // insurance expiry verification
        List<Insurance> insurance = loan.getLoanHolder().getInsurance();
        if (insurance != null) {
            HelperDto<Long> dto = new HelperDto<>(loan.getId(), HelperIdType.LOAN);
            insuranceService.execute(Optional.of(dto));
        }
        // check customer document
        List<Document> requiredDocuments;
        switch (loan.getLoanType()) {
            case NEW_LOAN:
                requiredDocuments = loan.getLoan().getInitial() == null ? new ArrayList<>()
                    : loan.getLoan().getInitial();
                break;
            case RENEWED_LOAN:
                requiredDocuments = loan.getLoan().getRenew() == null ? new ArrayList<>()
                    : loan.getLoan().getRenew();
                break;
            case ENHANCED_LOAN:
                requiredDocuments = loan.getLoan().getEnhance() == null ? new ArrayList<>()
                    : loan.getLoan().getEnhance();
                break;
            case CLOSURE_LOAN:
                requiredDocuments = loan.getLoan().getClosure() == null ? new ArrayList<>()
                    : loan.getLoan().getClosure();
                break;
            case PARTIAL_SETTLEMENT_LOAN:
                requiredDocuments =
                    loan.getLoan().getPartialSettlement() == null ? new ArrayList<>()
                        : loan.getLoan().getPartialSettlement();
                break;
            case FULL_SETTLEMENT_LOAN:
                requiredDocuments = loan.getLoan().getFullSettlement() == null ? new ArrayList<>()
                    : loan.getLoan().getFullSettlement();
                break;
            default:
                requiredDocuments = new ArrayList<>();
        }

        requiredDocuments = requiredDocuments
            .stream()
            .filter(d -> d.getCheckType() != null)
            .filter(d -> d.getCheckType().equals(DocumentCheckType.REQUIRED))
            .collect(Collectors.toList());
        List<Long> uploadedDocIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(loan.getCustomerDocument())) {
            uploadedDocIds = loan.getCustomerDocument().stream()
                .map(CustomerDocument::getDocument)
                .map(Document::getId)
                .collect(Collectors.toList());
        }
        List<Long> finalUploadedDocIds = uploadedDocIds;
        boolean missingRequired = !requiredDocuments.stream()
            .allMatch(d -> finalUploadedDocIds.contains(d.getId()));

        CustomerLoanFlag customerLoanFlag = loan.getLoanHolder().getLoanFlags().stream()
            .filter(loanFlag -> (loanFlag.getFlag().equals(LoanFlag.MISSING_REQUIRED_DOCUMENT)
                && loanFlag.getCustomerLoanId().equals(loan.getId())
            )).collect(CustomerLoanFlag.toSingleton());

        if (missingRequired && customerLoanFlag == null) {
            customerLoanFlag = new CustomerLoanFlag();
            customerLoanFlag.setCustomerInfo(loan.getLoanHolder());
            customerLoanFlag.setCustomerLoanId(loan.getId());
            customerLoanFlag.setFlag(LoanFlag.MISSING_REQUIRED_DOCUMENT);
            customerLoanFlag.setDescription(LoanFlag.MISSING_REQUIRED_DOCUMENT.getValue()[1]);
            customerLoanFlag
                .setOrder(Integer.parseInt(LoanFlag.MISSING_REQUIRED_DOCUMENT.getValue()[0]));
            customerLoanFlagService.save(customerLoanFlag);
        } else if (!missingRequired && customerLoanFlag != null) {
            customerLoanFlagService.deleteCustomerLoanFlagById(customerLoanFlag.getId());
        }

    }

    @Override
    public List<CustomerLoan> findAllBySpec(Map<String, String> filterParams) {
        CustomerLoanSpecBuilder builder = new CustomerLoanSpecBuilder(filterParams);
        return customerLoanRepository.findAll(builder.build());
    }

    @Override
    public List<CustomerLoan> findByCombinedLoanId(long id) {
        return customerLoanRepository.findAllByCombinedLoanId(id);
    }

    @Override
    public String title() {
        return "Form Report For Loan";
    }

    @Override
    public String subTitle() {
        return "Generated on: " + LocalDate.now() + " || Total Customers: " + Long
            .toString(this.countCustomer) + " || Total Branches: " + Long
            .toString(this.countBranch) + " || Total Loan Amount: " + totalLoan.toString();
    }


    @Override
    public List<AbstractColumn> columns() {
        AbstractColumn columnProvince = ColumnBuilder.getNew()
            .setColumnProperty("province.province.name", String.class.getName())
            .setTitle("Province").setWidth(new Integer(100))
            .build();

        AbstractColumn columnCreatedAt = ColumnBuilder.getNew()
            .setColumnProperty("createdAt", String.class.getName())
            .setTitle("Received Date").setWidth(new Integer(100))
            .build();

        AbstractColumn columnLoanId = ColumnBuilder.getNew()
            .setColumnProperty("loanIdCode", String.class.getName())
            .setTitle("Loan Id").setWidth(new Integer(150))
            .build();

        AbstractColumn columnBranch = ColumnBuilder.getNew()
            .setColumnProperty("branch.name", String.class.getName())
            .setTitle("Branch").setWidth(new Integer(100))
            .build();

        AbstractColumn columnName = ColumnBuilder.getNew()
            .setColumnProperty("data", String.class.getName())
            .setTitle("Customer Info").setWidth(new Integer(300))
            .build();

        AbstractColumn columnBusinessType = ColumnBuilder.getNew()
            .setColumnProperty("loanHolder.clientType", ClientType.class.getName())
            .setTitle("Business Unit").setWidth(new Integer(100))
            .build();

        AbstractColumn columnTypes = ColumnBuilder.getNew()
            .setColumnProperty("loanType", LoanType.class.getName())
            .setTitle("Proposal Type").setWidth(new Integer(100))
            .build();

        AbstractColumn columnProposedAmount = ColumnBuilder.getNew()
            .setColumnProperty("proposal.proposedLimit", BigDecimal.class.getName())
            .setTitle("Total Limit").setWidth(new Integer(100))
            .build();

        AbstractColumn columnCurrentPosition = ColumnBuilder.getNew()
            .setColumnProperty("toUser.name", String.class.getName())
            .setTitle("Picked By").setWidth(new Integer(100))
            .build();

        AbstractColumn columnStageUsers = ColumnBuilder.getNew()
            .setColumnProperty("stageUsers", String.class.getName())
            .setTitle("Last Returned By").setWidth(new Integer(300))
            .build();

        return Arrays.asList(
            columnName, columnProvince, columnCreatedAt, columnLoanId, columnBranch,
            columnBusinessType, columnTypes, columnProposedAmount, columnCurrentPosition,
            columnStageUsers
        );
    }


    @Override
    public ReportParam populate(Optional optional) {
        final User u = userService.getAuthenticatedUser();
        Map<String, String> s = objectMapper.convertValue(optional.get(), Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("branchIds", branchAccess);
        boolean isPullCsv = false;
        if (s.get("committee") != null) {
            isPullCsv = true;
        }
        if (u.getRole().getRoleType().equals(RoleType.COMMITTEE) && isPullCsv) {
            s.put("currentUserRole", u.getRole().getId().toString());
        }
        final CustomerLoanSpecBuilder customerLoanSpecBuilder = new CustomerLoanSpecBuilder(s);
        final Specification<CustomerLoan> specification = customerLoanSpecBuilder.build();
        final List<CustomerLoan> customerLoanList = customerLoanRepository.findAll(specification);
        final List<CustomerLoan> tempList = customerLoanList.stream()
            .sorted(
                (o1, o2) -> o1.getLoanHolder().getName().compareTo(o2.getLoanHolder().getName()))
            .collect(Collectors.toList());

        List csvDto = new ArrayList();
        this.countBranch = customerLoanList.stream()
            .map(c -> c.getBranch().getBranchCode())
            .distinct()
            .count();
        this.countCustomer = customerLoanList.stream()
            .map(c -> c.getLoanHolder().getId())
            .distinct()
            .count();
        this.totalLoan = customerLoanList.stream()
            .map(c -> c.getProposal().getProposedLimit())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (CustomerLoan c : tempList) {
            CustomerLoanCsvDto customerLoanCsvDto = new CustomerLoanCsvDto();
            customerLoanCsvDto.setBranch(c.getBranch());
            customerLoanCsvDto.setProvince(c.getBranch());
            customerLoanCsvDto.setCustomerInfo(c.getCustomerInfo());
            customerLoanCsvDto.setCompanyInfo(c.getCompanyInfo());
            customerLoanCsvDto.setLoanHolder(c.getLoanHolder());
            customerLoanCsvDto.setLoanIdCode(c.getRefNo());

            customerLoanCsvDto.setData(c.getLoanHolder().getName());
            customerLoanCsvDto.setLoan(c.getLoan());
            customerLoanCsvDto.setProposal(c.getProposal());
            customerLoanCsvDto.setLoanType(c.getLoanType());
            customerLoanCsvDto.setLoanCategory(c.getLoanCategory());
            customerLoanCsvDto.setDocumentStatus(c.getDocumentStatus());
            customerLoanCsvDto.setToUser(c.getCurrentStage().getToUser());
            customerLoanCsvDto.setCreatedAt(formatCsvDate(c.getCurrentStage().getCreatedAt()));
            StringBuilder stringBuilder1 = new StringBuilder()
                .append(c.getCurrentStage().getFromUser().getName());
            customerLoanCsvDto.setStageUsers(stringBuilder1.toString());
            if (c.getDocumentStatus() == DocStatus.PENDING
                || c.getDocumentStatus() == DocStatus.DOCUMENTATION
                || c.getDocumentStatus() == DocStatus.VALUATION
                || c.getDocumentStatus() == DocStatus.UNDER_REVIEW
                || c.getDocumentStatus() == DocStatus.DISCUSSION) {
                customerLoanCsvDto.setLoanPendingSpan(
                    this.calculatePendingLoanSpanAndPossession(c.getCurrentStage().getCreatedAt()));
                customerLoanCsvDto.setLoanPossession(
                    this.calculatePendingLoanSpanAndPossession(
                        c.getCurrentStage().getLastModifiedAt()));
            } else {
                customerLoanCsvDto.setLoanPossession(
                    this.calculateLoanSpanAndPossession(c.getCurrentStage().getLastModifiedAt(),
                        c.getPreviousList().get(c.getPreviousList().size() - 1)
                            .getLastModifiedAt()));
                customerLoanCsvDto.setLoanSpan(
                    this.calculateLoanSpanAndPossession(c.getCurrentStage().getLastModifiedAt(),
                        c.getCurrentStage().getCreatedAt()));
            }

            csvDto.add(customerLoanCsvDto);

        }

        String filePath = getDownloadPath();
        GroupBuilder gb1 = new GroupBuilder();
        //		 define the criteria column to group by (columnState)
        DJGroup g1 = gb1.setCriteriaColumn((PropertyColumn) columns().get(0))
            .setGroupLayout(new GroupLayout(false, false, false, true, false))
            .build();

        return ReportParam.builder().reportName("Catalogue Report")
            .title(title())
            .subTitleStyle(subTitleStyle())
            .subTitle(subTitle())
            .djGroups(Arrays.asList(g1))
            .columns(columns())
            .data(csvDto)
            .reportType(ReportType.FORM_REPORT)
            .filePath(UploadDir.WINDOWS_PATH + filePath)
            .exportType(ExportType.XLS)
            .build();

    }

    private Style subTitleStyle() {
        return new StyleBuilder(false).setFont(new Font(14, Font._FONT_ARIAL, true)).build();
    }

    public String getDownloadPath() {
        return new PathBuilder(UploadDir.initialDocument)
            .buildBuildFormDownloadPath("Catalogue");
    }

    private CustomerLoan mapLoanHolderToCustomerLoan(CustomerLoan customerLoan) {
        CustomerInfo customerInfo = customerLoan.getLoanHolder();
        customerLoan.setSecurity(customerInfo.getSecurity());
        customerLoan.setFinancial(customerInfo.getFinancial());
        customerLoan.setCreditRiskGrading(customerInfo.getCreditRiskGrading());
        customerLoan.setSiteVisit(customerInfo.getSiteVisit());
        //customerLoan.setGuarantor(customerInfo.getGuarantors());
        customerLoan.setInsurance(customerInfo.getInsurance());
        customerLoan.setShareSecurity(customerInfo.getShareSecurity());
        if (customerInfo.getCustomerType().equals(CustomerType.INSTITUTION)) {
            if (!customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                customerLoan.setCompanyInfo(
                    companyInfoService.findOne(customerLoan.getLoanHolder().getAssociateId()));
            }
        } else {
            if (!customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                customerLoan
                    .setCustomerInfo(customerService.findOne(customerInfo.getAssociateId()));
            }
        }
        if (ObjectUtils.isEmpty(customerLoan.getTaggedGuarantors())) {
            Set<Guarantor> data = new HashSet<>();
            customerLoan.setTaggedGuarantors(data);
        }

        return customerLoan;
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

    @Override
    public CustomerLoan saveCadLoanDocument(Long loanId, List<CadDocument> cadDocuments,
        String data) {
        CustomerLoan customerLoan = customerLoanRepository.findById(loanId).orElse(null);
        if (customerLoan == null) {
            throw new ServiceValidationException("No customer loan found!!!");
        }

        List<CadDocument> actualDocuments = customerLoan.getCadDocument();
        List<CadDocument> updatedDocuments = cadDocuments;
        for (CadDocument doc : actualDocuments) {
            Optional<CadDocument> optionalCadDocument = updatedDocuments.stream().filter(d -> Objects.equals(d.getId(), doc.getId())).findAny();
            if (!optionalCadDocument.isPresent()) {
                cadDocumentService.deleteByLoanIdAndDocument(loanId, doc.getId());
                cadDocumentService.deleteById(doc.getId());
            }
        }
        customerLoan.setData(data);
        customerLoan.setCadDocument(cadDocumentService.saveAll(cadDocuments));
        return customerLoanRepository.save(customerLoan);
    }

    public CustomerLoan saveCbsNumbers(CustomerLoan customerLoans) {
        CustomerLoan customerLoan1 = customerLoanRepository.findById(customerLoans.getId())
            .orElse(null);
        if (customerLoan1 == null) {
            throw new ServiceValidationException("No customer loan found!!!");
        } else if (!customerLoan1.getDocumentStatus().equals(DocStatus.APPROVED)) {
            throw new ServiceValidationException("Document Status should be Approved!!!");
        }
        customerLoan1.setCbsLoanFileNumber(customerLoans.getCbsLoanFileNumber());
        return customerLoanRepository.save(customerLoan1);
    }

    /**
     * Mega Bank Specific Method
     */

    @Override
    public Page<CustomerInfoLoanDto> getLoanByCustomerInfo(Object searchDto, Pageable pageable) {
        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        User u = userService.getAuthenticatedUser();
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("currentUserRole", u.getRole() == null ? null : u.getRole().getId().toString());
        s.put("toUser", u == null ? null : u.getId().toString());
        s.put("branchIds", branchAccess);
        Page<CustomerInfoLoanDto> customerInfoLoanDtoPage = criteriaSearch(s, pageable);

        List<CustomerInfoLoanDto> finalDtos = customerInfoLoanDtoPage.getContent();
        List<CustomerInfoLoanDto> list = new ArrayList<>();
        finalDtos.forEach(customerInfoLoanDto -> {
                s.put("loanHolderId", customerInfoLoanDto.getId().toString());
                logger.info("filter params :: Loan :::{}", s);
                CustomerLoanSpecBuilder customerLoanSpecBuilderInner = new CustomerLoanSpecBuilder(s);
                Specification<CustomerLoan> innerSpec = customerLoanSpecBuilderInner.build();
                // List<CustomerLoan> customerLoanList = customerLoanRepository.findAll(innerSpec);
                List<CustomerLoanFilterDto> results = customerBaseLoanFetch(innerSpec);
                List<CustomerLoanFilterDto> singleLoanList = results.stream()
                    .filter(f -> ObjectUtils.isEmpty(f.getCombinedLoan())).collect(
                        Collectors.toList());
                List<CustomerLoanFilterDto> combineLoanListMap = results.stream()
                    .filter(f -> !ObjectUtils.isEmpty(f.getCombinedLoan()))
                    .collect(Collectors.toList());
                List<CustomerLoanFilterDto> combineLoanListMapDistinct = combineLoanListMap.stream()
                    .filter(FilterJsonUtils.distinctByKey(o -> o.getCombinedLoan().getId())).collect(
                        Collectors.toList());

                List<Map<Long, List<CustomerLoanFilterDto>>> maps = new ArrayList<>();
                combineLoanListMapDistinct.forEach(map -> {
                    Map<Long, List<CustomerLoanFilterDto>> m = new HashMap<>();

                    List<CustomerLoanFilterDto> customerLoanList1 = combineLoanListMap.stream()
                        .filter(f ->
                            Objects.equals(f.getCombinedLoan().getId(), map.getCombinedLoan().getId()))
                        .collect(
                            Collectors.toList());

                    m.put(map.getCombinedLoan().getId(), customerLoanList1);
                    maps.add(m);


                });

                customerInfoLoanDto.setCombineList(maps);
                customerInfoLoanDto.setLoanSingleList(singleLoanList);
                list.add(customerInfoLoanDto);

            }
        );

        return new PageImpl<>(list, pageable, customerInfoLoanDtoPage.getTotalElements());

    }

    @Override
    public Page<CustomerInfoLoanDto> getLoanByCustomerInfoCommitteePULL(Object searchDto,
        Pageable pageable) {
        User u = userService.getAuthenticatedUser();
        if (u.getRole().getRoleType().equals(RoleType.COMMITTEE)) {
            Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
            String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
            if (s.containsKey("branchIds")) {
                branchAccess = s.get("branchIds");
            }
            s.put("branchIds", branchAccess);
            s.put("currentUserRole", u.getRole().getId().toString());
            s.values().removeIf(Objects::isNull);
            logger.info("query for pull {}", s);
            Page<CustomerInfoLoanDto> customerInfoLoanDtoPage = criteriaSearch(s, pageable);

            List<CustomerInfoLoanDto> finalDtos = customerInfoLoanDtoPage.getContent();
            List<CustomerInfoLoanDto> list = new ArrayList<>();
            finalDtos.forEach(customerInfoLoanDto -> {
                s.put("loanHolderId", customerInfoLoanDto.getCustomerInfo().getId().toString());
                logger.info("filter params {}", s);
                CustomerLoanSpecBuilder customerLoanSpecBuilderInner = new CustomerLoanSpecBuilder(
                    s);
                Specification<CustomerLoan> innerSpec = customerLoanSpecBuilderInner.build();
                // List<CustomerLoan> customerLoanList = customerLoanRepository.findAll(innerSpec);
                List<CustomerLoanFilterDto> results = customerBaseLoanFetch(innerSpec);
                for (CustomerLoanFilterDto c : results) {

                    if (Objects.requireNonNull(c.getCurrentStage().getToUser().getId())
                        .equals(u.getId())) {
                        c.setPulled(true);
                    }
                    for (LoanStageDto l : c.getPreviousList()) {
                        if (l.getToUser().getId().equals(u.getId())) {
                            c.setPulled(true);
                            break;
                        }
                    }

                }
                List<CustomerLoanFilterDto> singleLoanList = results.stream()
                    .filter(f -> ObjectUtils.isEmpty(f.getCombinedLoan())).collect(
                        Collectors.toList());
                List<CustomerLoanFilterDto> combineLoanListMap = results.stream()
                    .filter(f -> !ObjectUtils.isEmpty(f.getCombinedLoan()))
                    .collect(Collectors.toList());
                List<CustomerLoanFilterDto> combineLoanListMapDistinct = combineLoanListMap.stream()
                    .filter(FilterJsonUtils.distinctByKey(o -> o.getCombinedLoan().getId()))
                    .collect(
                        Collectors.toList());

                List<Map<Long, List<CustomerLoanFilterDto>>> maps = new ArrayList<>();
                combineLoanListMapDistinct.forEach(map -> {
                    Map<Long, List<CustomerLoanFilterDto>> m = new HashMap<>();

                    List<CustomerLoanFilterDto> customerLoanList1 = combineLoanListMap.stream()
                        .filter(f ->
                            Objects
                                .equals(f.getCombinedLoan().getId(), map.getCombinedLoan().getId()))
                        .collect(
                            Collectors.toList());

                    m.put(map.getCombinedLoan().getId(), customerLoanList1);
                    maps.add(m);


                });

                customerInfoLoanDto.setCombineList(maps);
                customerInfoLoanDto.setLoanSingleList(singleLoanList);
                list.add(customerInfoLoanDto);

            });
            return new PageImpl<>(list, pageable, customerInfoLoanDtoPage.getTotalElements());
        }
        return null;
    }

    @Override
    public Boolean checkCustomerIsEditable(Long loanHolderId) {
        if (userService.getAuthenticatedUser().getRole().getRoleType().equals(RoleType.MAKER)) {
            return !customerLoanRepository
                .existsByLoanHolderIdAndDocumentStatusAndCurrentStageToRoleRoleTypeNot(loanHolderId,
                    DocStatus.PENDING, RoleType.MAKER);
        } else {
            return false;
        }


    }

    @Override
    public void changeLoan(Long customerLoanId, Long loanConfigId) {
        final User u = userService.getAuthenticatedUser();
        if (u.getRole().getRoleType() == RoleType.MAKER) {
            LoanConfig loanConfig = loanConfigService.findOne(loanConfigId);
            CustomerLoan customerLoan = customerLoanRepository.getOne(customerLoanId);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            List previousList = customerLoan.getPreviousList();
            List previousListTemp = new ArrayList();
            LoanStage loanStage = new LoanStage();
            if (customerLoan.getCurrentStage() != null) {
                loanStage = customerLoan.getCurrentStage();
                if (!loanStage.getToUser().equals(u)) {
                    throw new ServiceValidationException(
                        "This Loan is not under you");
                }
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
            loanStage.setFromRole(u.getRole());
            loanStage.setFromUser(u);
            loanStage.setToRole(u.getRole());
            loanStage.setToUser(u);
            loanStage.setDocAction(DocAction.CHANGE_LOAN);
            loanStage.setComment(
                "Loan has been Changed From " + customerLoan.getLoan().getName() + " to "
                    + loanConfig.getName());
            customerLoanRepository
                .updateLoanConfigByCustomerLoanId(loanConfig, customerLoanId, loanStage,
                    previousListTemp.toString());
        } else {
            throw new ServiceValidationException("You do not have permission to perform Task");
        }
    }


    private Page<CustomerInfoLoanDto> criteriaSearch(Map<String, String> s, Pageable pageable) {
        logger.info("filter Customer ::: {}", s);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CustomerInfoLoanDto> q = cb.createQuery(CustomerInfoLoanDto.class);
        Root<CustomerLoan> root = q.from(CustomerLoan.class);

        q.select(
            cb.construct(
                CustomerInfoLoanDto.class,
                root.get("loanHolder").get("name"),
                root.get("loanHolder").get("customerType"),
                root.get("loanHolder").get("id"),
                root.get("loanHolder").get("clientType"),
                root.get("loanHolder").get("customerCode"),
                root.get("loanHolder").get("contactNo"),
                root.get("loanHolder").get("branch").get("name"),
                root.get("loanHolder").get("branch").get("province").get("name")
            )).distinct(true);

        CustomerLoanSpecBuilder customerLoanSpecBuilderInner = new CustomerLoanSpecBuilder(s);
        Specification<CustomerLoan> innerSpec = customerLoanSpecBuilderInner.build();
        List<CustomerInfoLoanDto> resultList = em
            .createQuery(q.where(innerSpec.toPredicate(root, q, cb)))
            .setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize())
            .getResultList();

        // Create Count Query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CustomerLoan> customerLoanRootCount = countQuery.from(CustomerLoan.class);
        countQuery.select(
            cb.countDistinct(customerLoanRootCount.get("loanHolder").get("id")));
        Long count = em.createQuery(
            countQuery.where(innerSpec.toPredicate(customerLoanRootCount, countQuery, cb)))
            .getSingleResult();
        return new PageImpl<CustomerInfoLoanDto>(resultList, pageable, count);


    }

    @Override
    public void transferLoanToOtherBranch(CustomerLoan customerLoan, Long branchId,
        User currentUser) {
        if (currentUser.getRole().getRoleType().equals(RoleType.ADMIN)
            || currentUser.getUsername().equalsIgnoreCase("SPADMIN")) {
            if (customerLoan.getCurrentStage() == null
                || customerLoan.getCurrentStage().getToRole() == null
                || customerLoan.getCurrentStage().getToUser() == null) {
                logger.warn("Empty current Stage{}", customerLoan.getCurrentStage());
                throw new ServiceValidationException("Unable to perform task");
            }
            String oldBranchName = customerLoan.getBranch().getName();
            Branch newBranch = this.branchService.findOne(branchId);
            customerLoan.setBranch(newBranch);
            CustomerLoan customerLoan1 = customerLoanRepository.save(customerLoan);
            CustomerActivity customerActivity = null;
            try {
                customerActivity = CustomerActivity.builder()
                    .customerLoanId(customerLoan1.getId())
                    .activityType(ActivityType.MANUAL)
                    .activity(Activity.LOAN_TRANSFER)
                    .modifiedOn(new Date())
                    .modifiedBy(currentUser)
                    .profile(customerLoan1.getLoanHolder())
                    .data(objectMapper.writeValueAsString(customerLoan1))
                    .description(String
                        .format("%s has been successfully transferred from %s branch to %s branch",
                            customerLoan1.getLoan().getName(), oldBranchName, newBranch.getName()))
                    .build();
                activityService.saveCustomerActivity(customerActivity);
            } catch (JsonProcessingException e) {
                throw new ServiceValidationException(
                    "Unable to add customer activity!");
            }
            oldBranchName = null;
        } else {
            throw new ServiceValidationException(
                "Transfer Failed: You are not authorized to perform this action!!!");
        }
    }

    @Override
    public void deleteLoanByMakerAndAdmin(Long customerLoanId) {

        final User u = userService.getAuthenticatedUser();
        if (u.getRole().getRoleType().equals(RoleType.MAKER) || (u.getRole().getRoleType()
            .equals(RoleType.ADMIN))) {
            final CustomerLoan customerLoan = customerLoanRepository.getOne(customerLoanId);
            if (!ObjectUtils.isEmpty(customerLoan.getChildId())) {
                throw new ServiceValidationException(
                    "This file Cannot be Deleted .Its in hirerachy");
            }
            LoanStage l = customerLoan.getCurrentStage();
            if (u.getRole().getRoleType().equals(RoleType.MAKER)) {
                if (l.getToUser().equals(u)) {
                    customerLoanRepository.deleteById(customerLoanId);
                    if (!ObjectUtils.isEmpty(customerLoan.getParentId())) {
                        //remove child Id from parent Id;
                        customerLoanRepository
                            .updateCloseRenewChildId(null, customerLoan.getParentId(),
                                false);
                    }
                }

            } else {
                if (l.getToRole().getRoleType().equals(RoleType.MAKER)) {
                    customerLoanRepository.deleteById(customerLoanId);
                    if (!ObjectUtils.isEmpty(customerLoan.getParentId())) {
                        //remove child Id from parent Id;
                        customerLoanRepository
                            .updateCloseRenewChildId(null, customerLoan.getParentId(),
                                false);

                    }
                }

            }
            try {
                CustomerActivity customerActivity = CustomerActivity.builder()
                    .customerLoanId(customerLoan.getId())
                    .activityType(ActivityType.MANUAL)
                    .activity(Activity.DELETE_LOAN)
                    .modifiedOn(new Date())
                    .modifiedBy(u)
                    .profile(customerLoan.getLoanHolder())
                    .data(objectMapper.writeValueAsString(customerLoan))
                    .description(String
                        .format("Customer: %s loan Facility: %s deleted by %s",
                            customerLoan.getLoanHolder().getName(),
                            customerLoan.getLoan().getName(), u.getName()))
                    .build();
                activityService.saveCustomerActivity(customerActivity);
            } catch (JsonProcessingException e) {
                throw new ServiceValidationException(
                    "Unable to add customer activity!");
            }
        } else {
            throw new ServiceValidationException("You don't have Permission to delete this file");
        }
    }

    private List<CustomerLoanFilterDto> customerBaseLoanFetch(
        Specification<CustomerLoan> innerSpec) {
        String[] columns = {"id", "loan.name", "loanType", "documentStatus", "currentStage",
            "priority", "previousStageList", "combinedLoan", "proposal", "loan.id"};
        String[] joinColumn = {"combinedLoan"};
        CriteriaDto<CustomerLoan, CustomerLoanFilterDto> criteriaDto = new CriteriaDto<>(
            CustomerLoan.class, CustomerLoanFilterDto.class, innerSpec, columns, joinColumn);
        BaseCriteriaQuery<CustomerLoan, CustomerLoanFilterDto> baseCriteriaQuery = new BaseCriteriaQuery<>();
        return baseCriteriaQuery.getList(criteriaDto, em);
    }

    @Transactional
    @Override
    public CustomerLoan saveLoan(CustomerLoan customerLoan, User loggedInUser) {
        if (!loggedInUser.getRole().getRoleType().equals(RoleType.MAKER)) {
            throw new ServiceValidationException("You don't have privilege to save loan");
        }
        // validation start
        if (customerLoan.getLoan() == null) {
            throw new ServiceValidationException("Loan can not be null");
        }

        boolean isNewLoan = false;

        // If new loan
        if (customerLoan.getId() == null) {
            isNewLoan = true;
            customerLoan.setBranch(userService.getAuthenticatedUser().getBranch().get(0));
            LoanStage stage = new LoanStage();
            stage.setToRole(userService.getAuthenticatedUser().getRole());
            stage.setFromRole(userService.getAuthenticatedUser().getRole());
            stage.setFromUser(userService.getAuthenticatedUser());
            stage.setToUser(userService.getAuthenticatedUser());
            stage.setComment(DocAction.DRAFT.name());
            stage.setDocAction(DocAction.DRAFT);
            customerLoan.setCurrentStage(stage);
        }

        // If old loan
        if (!isNewLoan) {
            // Check if loan is editable by current user
            if (!isLoanEditable(customerLoan, loggedInUser)) {
                throw new ServiceValidationException("Loan is not editable. It is not in your custody");
            }
            CustomerLoan beforeSave = customerLoanRepository.getOne(customerLoan.getId());
            List<CustomerDocument> actualDocuments = beforeSave.getCustomerDocument();
            List<CustomerDocument> updatedDocuments = customerLoan.getCustomerDocument();

            for (CustomerDocument doc : actualDocuments) {
                Optional<CustomerDocument> optionalCustomerDocument = updatedDocuments.stream().filter(d -> Objects.equals(d.getId(), doc.getId())).findAny();
                if (!optionalCustomerDocument.isPresent()) {
                    customerDocumentService.deleteById(doc.getId());
                }
            }
        }

        if (customerLoan.getFinancial() != null) {
            customerLoan.setFinancial(customerLoan.getLoanHolder().getFinancial());
        }
        if (customerLoan.getSecurity() != null) {
            customerLoan.setSecurity(customerLoan.getLoanHolder().getSecurity());
        }
        if (customerLoan.getSiteVisit() != null) {
            customerLoan.setSiteVisit(customerLoan.getLoanHolder().getSiteVisit());
        }
        if (customerLoan.getProposal() != null) {
            customerLoan.setProposal(this.proposalService.save(customerLoan.getProposal()));
        }
        if (customerLoan.getCreditRiskGrading() != null) {
            customerLoan.setCreditRiskGrading(
                    customerLoan.getLoanHolder().getCreditRiskGrading());
        }
        if (customerLoan.getGroup() != null) {
            customerLoan.setGroup(this.groupService.save(customerLoan.getGroup()));
        }
        if (customerLoan.getVehicleSecurity() != null) {
            customerLoan
                    .setVehicleSecurity(null);
        }

        if (customerLoan.getCreditRiskGradingAlpha() != null) {
            customerLoan.setCreditRiskGradingAlpha(
                    this.creditRiskGradingAlphaService.save(customerLoan.getCreditRiskGradingAlpha()));
        }

        if (customerLoan.getCreditRiskGradingLambda() != null) {
            customerLoan.setCreditRiskGradingLambda(this.creditRiskGradingLambdaService
                    .save(customerLoan.getCreditRiskGradingLambda()));
        }

        if (customerLoan.getCrgMicro() != null) {
            customerLoan.setCrgMicro(this.crgMicroService
                    .save(customerLoan.getCrgMicro()));
        }

        if (customerLoan.getCrgGamma() != null) {
            customerLoan.setCrgGamma(this.crgGammaService.save(customerLoan.getCrgGamma()));
        }

        if (customerLoan.getShareSecurity() != null) {
            customerLoan
                    .setShareSecurity(customerLoan.getLoanHolder().getShareSecurity());
        }
        if (customerLoan.getInsurance() != null) {
            customerLoan.setInsurance(customerLoan.getLoanHolder().getInsurance());
        }

        CustomerLoan savedCustomerLoan = customerLoanRepository.save(customerLoan);
        postLoanConditionCheck(savedCustomerLoan);

        if (!customerLoan.getNepaliTemplates().isEmpty()) {
            List<NepaliTemplate> nepaliTemplates = nepaliTemplateMapper
                    .mapDtosToEntities(customerLoan.getNepaliTemplates());
            nepaliTemplates.forEach(v -> v.setCustomerLoan(savedCustomerLoan));
            nepaliTemplateService.saveAll(nepaliTemplates);
        }

        if (isNewLoan) {
            List<CustomerDocument> customerDocuments = savedCustomerLoan.getCustomerDocument();
            customerDocuments.forEach(customerDocument -> {
                String docPath = customerDocument.getDocumentPath();
                String documentPath[] = docPath.split("tempDocument");
                documentPath[0] =
                        documentPath[0] + "loan-loan-" + savedCustomerLoan.getId() + "/" + "doc";
                String newDocPath = documentPath[0] + documentPath[1];
                customerDocument.setDocumentPath(newDocPath);
                FileUploadUtils.moveFile(docPath, newDocPath);
            });
            savedCustomerLoan.setCustomerDocument(customerDocuments);

            String refNumber = new StringBuilder().append(LocalDate.now().getYear())
                    .append(LocalDate.now().getMonthValue())
                    .append(LocalDate.now().getDayOfMonth()).append(SEPERATOR_FRONT_SLASH)
                    .append(customerLoan.getLoan().getId()).append(SEPERATOR_FRONT_SLASH)
                    .append(
                            StringUtil.getAcronym(customerLoan.getLoanCategory().name(), SEPERATOR_BLANK))
                    .append(SEPERATOR_FRONT_SLASH).append(customerLoan.getId()).toString();
            customerLoanRepository.updateReferenceNo(refNumber, customerLoan.getId());
        }
        return savedCustomerLoan;
    }

    private CustomerLoan createJsonFile(CustomerLoan customerLoan, User user) {
        try {
            if (!ObjectUtils.isEmpty(customerLoan.getLoanHolder().getCustomerGroup())
                    && this.customerGroupLogList.isEmpty()) {
                List<CustomerLoanDto> customerGroup = customerLoanRepositoryJdbcTemplate
                        .findByLoanHolderCustomerGroupAndNotToCurrentLoanHolder(
                                customerLoan.getLoanHolder().getCustomerGroup().getId(),
                                customerLoan.getLoanHolder().getId());
                customerLoan
                        .setGroupSummaryDto(
                                objectMapper
                                        .convertValue(customerGroup, List.class));
            } else {
                customerLoan
                        .setGroupSummaryDto(
                                objectMapper
                                        .convertValue(this.customerGroupLogList, List.class));
            }
            if (!customerLoan.getReportingInfoLevels().isEmpty()) {
                try {
                    List report = customerLoan.getReportingInfoLevels();
                    String reportingString = objectMapper.writeValueAsString(report);
                    customerLoan.setReportingInfoLevels(new ArrayList<>());
                    customerLoan.setReportingInfoLog(reportingString);
                } catch (Exception e) {
                    logger.error("unable to parse reporting Info");
                }
            }
            String basePath = new PathBuilder(UploadDir.initialDocument)
                    .buildLoanDocumentUploadBasePathWithId(customerLoan.getLoanHolder().getId(),
                            customerLoan.getLoanHolder().getBranch().getId(),
                            customerLoan.getLoanHolder().getCustomerType().name(),
                            actionType(customerLoan.getLoanType()),
                            customerLoan.getLoan().getId());
            String filePath = FilePath.getOSPath() + basePath;
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                new File(filePath).mkdirs();
            }
            if (customerLoan.getLoanHolder().getCustomerType()
                    .equals(CustomerType.INSTITUTION)) {
                customerLoan.setCompanyInfo(
                        companyInfoService.findOne(customerLoan.getLoanHolder().getAssociateId()));
            } else {
                customerLoan.setCustomerInfo(
                        customerService.findOne(customerLoan.getLoanHolder().getAssociateId()));
            }
            if (ObjectUtils.isEmpty(customerLoan.getTaggedGuarantors())) {
                customerLoan.setTaggedGuarantors(Collections.emptySet());
            }
            customerLoan.setVersion(customerLoan.getVersion() + 1);
            customerLoan.getCurrentStage().setVersion(customerLoan.getCurrentStage().getVersion() + 1);
            Map<String, String> map = new HashMap<>();
            if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                map.put("filePath", basePath + customerLoan.getId() + "-approved.json");
            } else if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                map.put("filePath", basePath + customerLoan.getId() + "-closed.json");
            } else {
                map.put("filePath", basePath + customerLoan.getId() + "-rejected.json");
            }
            new Thread(() -> {
                try {
                    if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                        objectMapper.writeValue(
                                Paths.get(filePath + customerLoan.getId() + "-approved.json")
                                        .toFile(), customerLoan);
                    } else if (customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
                        objectMapper.writeValue(
                                Paths.get(filePath + customerLoan.getId() + "-closed.json")
                                        .toFile(), customerLoan);
                    } else {
                        objectMapper.writeValue(
                                Paths.get(filePath + customerLoan.getId() + "-rejected.json")
                                        .toFile(), customerLoan);
                    }
                } catch (Exception e) {
                    logger.error(
                            "unable to write json file of customer {} loan {} with path {} with ex ::{}",
                            customerLoan.getLoanHolder().getName(),
                            customerLoan.getLoan().getName(),
                            filePath, e);
                }
            }).start();
            if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                CustomerActivity customerActivity = CustomerActivity.builder()
                        .customerLoanId(customerLoan.getId())
                        .activityType(ActivityType.MANUAL)
                        .activity(Activity.LOAN_APPROVED)
                        .modifiedOn(new Date())
                        .modifiedBy(user)
                        .profile(customerLoan.getLoanHolder())
                        .data(objectMapper.writeValueAsString(customerLoan))
                        .description(
                                String.format(DESCRIPTION_APPROVED, customerLoan.getLoan().getName()))
                        .build();
                activityService.saveCustomerActivity(customerActivity);
            } else if (customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
                CustomerActivity customerActivity = CustomerActivity.builder()
                        .customerLoanId(customerLoan.getId())
                        .activityType(ActivityType.MANUAL)
                        .activity(Activity.LOAN_CLOSED)
                        .modifiedOn(new Date())
                        .modifiedBy(user)
                        .profile(customerLoan.getLoanHolder())
                        .data(objectMapper.writeValueAsString(customerLoan))
                        .description(
                                String.format(DESCRIPTION_CLOSED, customerLoan.getLoan().getName()))
                        .build();
                activityService.saveCustomerActivity(customerActivity);
            } else {
                CustomerActivity customerActivity = CustomerActivity.builder()
                        .customerLoanId(customerLoan.getId())
                        .activityType(ActivityType.MANUAL)
                        .activity(Activity.LOAN_REJECT)
                        .modifiedOn(new Date())
                        .modifiedBy(user)
                        .profile(customerLoan.getLoanHolder())
                        .data(objectMapper.writeValueAsString(customerLoan))
                        .description(
                                String.format(DESCRIPTION_REJECTED, customerLoan.getLoan().getName()))
                        .build();
                activityService.saveCustomerActivity(customerActivity);
            }
        } catch (Exception e) {
            if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                logger.error("unable to log approved file of {} and loan {} with error e {} ",
                        customerLoan.getLoanHolder().getName(), customerLoan.getLoan().getName(), e);
            } else if (customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
                logger.error("unable to log closed file of {} and loan {} with error e {} ",
                        customerLoan.getLoanHolder().getName(), customerLoan.getLoan().getName(), e);
            } else {
                logger.error("unable to log rejected file of {} and loan {} with error e {} ",
                        customerLoan.getLoanHolder().getName(), customerLoan.getLoan().getName(), e);
            }
        }
        return customerLoan;
    }

    private CustomerLoan readJsonFile(CustomerLoan customerLoan, Long id) {
        String basePath = new PathBuilder(UploadDir.initialDocument)
                .buildLoanDocumentUploadBasePathWithId(customerLoan.getLoanHolder().getId(),
                        customerLoan.getBranch().getId(),
                        customerLoan.getLoanHolder().getCustomerType().name(),
                        actionType(customerLoan.getLoanType()),
                        customerLoan.getLoan().getId());
        String filePath;
        if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
            filePath = FilePath.getOSPath() + basePath + customerLoan.getId() + "-approved.json";
        } else if (customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
            filePath = FilePath.getOSPath() + basePath + customerLoan.getId() + "-closed.json";
        } else {
            filePath = FilePath.getOSPath() + basePath + customerLoan.getId() + "-rejected.json";
        }
        JSONParser jsonParser = new JSONParser();
        try {
            logger.info("reading json file of path :: {}", filePath);
            FileReader reader = new FileReader(filePath);
            Object obj = jsonParser.parse(reader);
            customerLoan = objectMapper.convertValue(obj, CustomerLoan.class);
        } catch (Exception e) {
            logger.error("unable to parse json file of customerLoanId {}", id);
            List<CustomerActivity> activity;
            if (customerLoan.getDocumentStatus().equals(DocStatus.APPROVED)) {
                activity = activityService
                        .findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(
                                Activity.LOAN_APPROVED, id);
            } else if (customerLoan.getDocumentStatus().equals(DocStatus.CLOSED)) {
                activity = activityService
                        .findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(
                                Activity.LOAN_CLOSED, id);
            } else {
                activity = activityService
                        .findCustomerActivityByActivityAndCustomerLoanIdOrderByModifiedOnAsc(
                                Activity.LOAN_REJECT, id);
            }
            if (!ObjectUtils.isEmpty(activity)) {
                CustomerActivity activity1 = activity.get(0);
                try {
                    customerLoan = objectMapper
                            .readValue(activity1.getData(), CustomerLoan.class);
                } catch (Exception ex) {
                    logger.error("unable to extract data {}", id);
                }
            }
        }
        return customerLoan;
    }
}

