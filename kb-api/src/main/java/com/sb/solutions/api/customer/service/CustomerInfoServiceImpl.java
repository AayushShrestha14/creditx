package com.sb.solutions.api.customer.service;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Preconditions;

import com.sb.solutions.api.borrowerPortfolio.entity.BorrowerPortFolio;
import com.sb.solutions.api.borrowerPortfolio.service.BorrowerPortFolioService;
import com.sb.solutions.api.branch.service.BranchService;
import com.sb.solutions.api.creditChecklist.entity.CreditChecklist;
import com.sb.solutions.api.creditChecklist.service.CreditChecklistService;
import com.sb.solutions.api.customer.repository.specification.CustomerInfoSpec;
import com.sb.solutions.api.loan.dto.CustomerListDto;
import com.sb.solutions.api.marketingActivities.MarketingActivities;
import com.sb.solutions.api.marketingActivities.service.MarketingActivitiesService;
import com.sb.solutions.api.microBorrowerFinancial.MicroBorrowerFinancial;
import com.sb.solutions.api.microBorrowerFinancial.service.MicroBorrowerFinancialService;
import com.sb.solutions.api.microOtherParameters.MicroOtherParameters;
import com.sb.solutions.api.microOtherParameters.service.MicroOtherParametersService;
import com.sb.solutions.api.microbaselriskexposure.entity.MicroBaselRiskExposure;
import com.sb.solutions.api.microbaselriskexposure.service.MicroBaselRiskExposureService;
import com.sb.solutions.api.netTradingAssets.entity.NetTradingAssets;
import com.sb.solutions.api.netTradingAssets.service.NetTradingAssetsService;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfoLevel;
import com.sb.solutions.api.reportinginfo.service.ReportingInfoLevelService;
import com.sb.solutions.api.synopsisOfCreditwothiness.entity.SynopsisCreditworthiness;
import com.sb.solutions.api.synopsisOfCreditwothiness.service.SynopsisCreditworthinessService;
import com.sb.solutions.core.repository.customCriteria.BaseCriteriaQuery;
import com.sb.solutions.core.repository.customCriteria.dto.CriteriaDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.sb.solutions.api.cicl.entity.Cicl;
import com.sb.solutions.api.cicl.service.CIclService;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.constant.TemplateNameConstant;
import com.sb.solutions.api.creditRiskGrading.entity.CreditRiskGrading;
import com.sb.solutions.api.creditRiskGrading.service.CreditRiskGradingService;
import com.sb.solutions.api.creditRiskGradingAlpha.entity.CreditRiskGradingAlpha;
import com.sb.solutions.api.creditRiskGradingAlpha.service.CreditRiskGradingAlphaService;
import com.sb.solutions.api.crg.entity.CrgGamma;
import com.sb.solutions.api.crg.service.CrgGammaService;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerIdType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customer.repository.CustomerInfoRepository;
import com.sb.solutions.api.customer.repository.specification.CustomerInfoSpecBuilder;
import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.financial.entity.Financial;
import com.sb.solutions.api.financial.service.FinancialService;
import com.sb.solutions.api.guarantor.entity.GuarantorDetail;
import com.sb.solutions.api.guarantor.service.GuarantorDetailService;
import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperIdType;
import com.sb.solutions.api.incomeFromAccount.entity.IncomeFromAccount;
import com.sb.solutions.api.incomeFromAccount.service.IncomeFromAccountServices;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.insurance.service.InsuranceService;
import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.api.security.service.SecurityService;
import com.sb.solutions.api.sharesecurity.ShareSecurity;
import com.sb.solutions.api.sharesecurity.service.ShareSecurityService;
import com.sb.solutions.api.siteVisit.entity.SiteVisit;
import com.sb.solutions.api.siteVisit.service.SiteVisitService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.ReportType;
import com.sb.solutions.report.core.factory.ReportFactory;
import com.sb.solutions.report.core.model.Report;

import javax.persistence.EntityManager;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
@Service
@Slf4j
public class CustomerInfoServiceImpl extends BaseServiceImpl<CustomerInfo, Long> implements
        CustomerInfoService {
    private static final String[] columns = {"id", "name", "idNumber",
            "customerType", "contactNo",
            "email", "idRegPlace", "idRegDate", "createdAt", "associateId",
            "branch.province.id", "customerGroup.id", "clientType","subsectorDetail",
    "customerCode", "bankingRelationship","gender","maritalStatus", "customerLegalDocumentAddress",
    "branch", "branch.id"};
    private static final String[] joinColumn = {"branch", "customerGroup"};

    private static final String NULL_MESSAGE = "Invalid customer info id,Data does not exist";

    private final CustomerInfoRepository customerInfoRepository;
    private final SiteVisitService siteVisitService;
    private final FinancialService financialService;
    private final SecurityService securityService;
    private final ShareSecurityService shareSecurityService;
    private final GuarantorDetailService guarantorDetailService;
    private final UserService userService;
    private final InsuranceService insuranceService;
    private final CreditRiskGradingAlphaService creditRiskGradingAlphaService;
    private final CreditRiskGradingService creditRiskGradingService;
    private final CrgGammaService crgGammaService;
    private final CustomerLoanFlagService loanFlagService;
    private final CIclService cIclService;
    private final IncomeFromAccountServices incomeFromAccountServices;
    private final NetTradingAssetsService netTradingAssetsService;
    private final CreditChecklistService creditChecklistService;
    private final SynopsisCreditworthinessService synopsisCreditworthinessService;
    private final BorrowerPortFolioService borrowerPortFolioService;
    private final MicroBaselRiskExposureService microBaselRiskExposureService;
    private final MicroBorrowerFinancialService microBorrowerFinancialService;
    private final MicroOtherParametersService microOtherParametersService;
    private final MarketingActivitiesService marketingActivitiesService;
    private final ReportingInfoLevelService reportingInfoLevelService;
    private final BranchService branchService;

    @Autowired
    EntityManager em;

    public CustomerInfoServiceImpl(
            @Autowired CustomerInfoRepository customerInfoRepository,
            FinancialService financialService,
            SiteVisitService siteVisitService,
            SecurityService securityService,
            ShareSecurityService shareSecurityService,
            GuarantorDetailService guarantorDetailService,
            UserService userService,
            InsuranceService insuranceService,
            CreditRiskGradingAlphaService creditRiskGradingAlphaService,
            CreditRiskGradingService creditRiskGradingService,
            CrgGammaService crgGammaService,
            CustomerLoanFlagService loanFlagService,
            IncomeFromAccountServices incomeFromAccountServices,
            NetTradingAssetsService netTradingAssetsService,
            CIclService cIclService,
            CreditChecklistService creditChecklistService,
            SynopsisCreditworthinessService synopsisCreditworthinessService,
            BorrowerPortFolioService borrowerPortFolioService,
            MicroBaselRiskExposureService microBaselRiskExposureService,
            MicroBorrowerFinancialService microBorrowerFinancialService,
            MicroOtherParametersService microOtherParametersService,
            MarketingActivitiesService marketingActivitiesService,
            ReportingInfoLevelService reportingInfoLevelService,
            BranchService branchService) {
        super(customerInfoRepository);
        this.customerInfoRepository = customerInfoRepository;
        this.financialService = financialService;
        this.siteVisitService = siteVisitService;
        this.userService = userService;
        this.securityService = securityService;
        this.shareSecurityService = shareSecurityService;
        this.guarantorDetailService = guarantorDetailService;
        this.insuranceService = insuranceService;
        this.creditRiskGradingAlphaService = creditRiskGradingAlphaService;
        this.creditRiskGradingService = creditRiskGradingService;
        this.crgGammaService = crgGammaService;
        this.loanFlagService = loanFlagService;
        this.cIclService = cIclService;
        this.incomeFromAccountServices = incomeFromAccountServices;
        this.netTradingAssetsService = netTradingAssetsService;
        this.creditChecklistService = creditChecklistService;
        this.synopsisCreditworthinessService = synopsisCreditworthinessService;
        this.borrowerPortFolioService = borrowerPortFolioService;
        this.microBaselRiskExposureService = microBaselRiskExposureService;
        this.microBorrowerFinancialService = microBorrowerFinancialService;
        this.microOtherParametersService = microOtherParametersService;
        this.marketingActivitiesService = marketingActivitiesService;
        this.reportingInfoLevelService = reportingInfoLevelService;
        this.branchService = branchService;
    }


    @Override
    public CustomerInfo saveObject(Object o) {
        CustomerInfo customerInfo = new CustomerInfo();
        User user = userService.getAuthenticatedUser();
        Preconditions.checkArgument(user.getRole().getRoleType() == RoleType.MAKER,
                "You are not Authorize to save customer info");
        if (o instanceof Customer) {
            customerInfo = customerInfoRepository
                    .findByAssociateIdAndCustomerType(((Customer) o).getId(), CustomerType.INDIVIDUAL);
            log.info("Saving customer into customer info {}", o);
            if (ObjectUtils.isEmpty(customerInfo)) {
                customerInfo = new CustomerInfo();
            }
            customerInfo.setAssociateId(((Customer) o).getId());
            customerInfo.setCustomerType(CustomerType.INDIVIDUAL);
            customerInfo.setIdNumber(((Customer) o).getCitizenshipNumber());
            customerInfo.setIdRegPlace(((Customer) o).getCitizenshipIssuedPlace());
            customerInfo.setIdRegDate(((Customer) o).getCitizenshipIssuedDate());
            customerInfo.setName(((Customer) o).getCustomerName());
            customerInfo.setIdType(CustomerIdType.CITIZENSHIP);
            customerInfo.setContactNo(((Customer) o).getContactNumber());
            customerInfo.setEmail(((Customer) o).getEmail());
            customerInfo.setCustomerCode(((Customer) o).getCustomerCode());
            customerInfo.setBankingRelationship(((Customer) o).getBankingRelationship());
            customerInfo.setClientType(((Customer) o).getClientType());
            customerInfo.setSubsectorDetail(((Customer) o).getSubsectorDetail());
            customerInfo.setGender(((Customer) o).getGender());
            customerInfo.setMaritalStatus(((Customer) o).getMaritalStatus());
            customerInfo
                    .setCustomerLegalDocumentAddress(((Customer) o).getCustomerLegalDocumentAddress());
            customerInfo.setJointInfo(((Customer) o).getJointInfo());
            customerInfo.setIsJointCustomer(((Customer) o).getIsJointCustomer());

        }

        if (o instanceof CompanyInfo) {

            customerInfo = customerInfoRepository
                    .findByAssociateIdAndCustomerType(((CompanyInfo) o).getId(),
                            CustomerType.INSTITUTION);
            log.info("Saving company into customer info {}", o);
            if (ObjectUtils.isEmpty(customerInfo)) {
                customerInfo = new CustomerInfo();
            }
            customerInfo.setAssociateId(((CompanyInfo) o).getId());
            customerInfo.setCustomerType(CustomerType.INSTITUTION);
            customerInfo.setName(((CompanyInfo) o).getCompanyName());
            customerInfo.setIdType(CustomerIdType.PAN);
            customerInfo.setIdRegDate(((CompanyInfo) o).getLegalStatus().getRegistrationDate());
            customerInfo.setIdNumber(((CompanyInfo) o).getPanNumber());
            customerInfo.setIdRegPlace(((CompanyInfo) o).getIssuePlace());
            customerInfo.setContactNo(((CompanyInfo) o).getContactNum());
            customerInfo.setEmail(((CompanyInfo) o).getEmail());
            customerInfo.setCustomerCode(((CompanyInfo) o).getCustomerCode());
            customerInfo.setBankingRelationship(((CompanyInfo) o).getBankingRelationship());
            customerInfo.setClientType(((CompanyInfo) o).getClientType());
            customerInfo.setSubsectorDetail(((CompanyInfo) o).getSubsectorDetail());
        }
        customerInfo.setBranch(user.getBranch().get(0));
        return this.save(customerInfo);
    }

    @Transactional
    @Override
    public CustomerInfo saveLoanInformation(Object o, Long customerInfoId, String template) {
        Optional<CustomerInfo> customerInfo = customerInfoRepository.findById(customerInfoId);
        Preconditions.checkArgument(customerInfo.isPresent(), NULL_MESSAGE);
        final CustomerInfo customerInfo1 = customerInfo.get();
        if ((template.equalsIgnoreCase(TemplateNameConstant.SITE_VISIT))) {
            final SiteVisit siteVisit = siteVisitService
                    .save(objectMapper().convertValue(o, SiteVisit.class));
            customerInfo1.setSiteVisit(siteVisit);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.FINANCIAL))) {

            final Financial financial = financialService
                    .save(objectMapper().convertValue(o, Financial.class));
            customerInfo1.setFinancial(financial);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.SECURITY))) {

            final Security security = securityService
                    .save(objectMapper().convertValue(o, Security.class));
            customerInfo1.setSecurity(security);
            HelperDto<Long> dto = new HelperDto<>(customerInfoId, HelperIdType.CUSTOMER_INFO);
            securityService.execute(Optional.of(dto));
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.SHARE_SECURITY))) {

            final ShareSecurity shareSecurity = shareSecurityService
                    .save(objectMapper().convertValue(o, ShareSecurity.class));
            customerInfo1.setShareSecurity(shareSecurity);
            HelperDto<Long> helperDto = new HelperDto<>(customerInfoId, HelperIdType.CUSTOMER_INFO);
            shareSecurityService.execute(Optional.of(helperDto));
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.GUARANTOR))) {
            final GuarantorDetail guarantors = guarantorDetailService
                    .save(objectMapper().convertValue(o, GuarantorDetail.class));
            customerInfo1.setGuarantors(guarantors);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.INSURANCE))) {
            ObjectMapper mapper = new ObjectMapper();
            List<Insurance> insurances = Arrays.asList(mapper.convertValue(o, Insurance[].class));
            final List<Insurance> insurance = insuranceService.saveAll(insurances);
            customerInfo1.setInsurance(insurance);
            // TODO enable this for insurance expiry
//            insuranceService
//                .execute(Optional.of(new HelperDto<>(customerInfoId, HelperIdType.CUSTOMER_INFO)));
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CUSTOMER_GROUP))) {
            CustomerGroup customerGroup = objectMapper().convertValue(o, CustomerGroup.class);
            if (customerGroup.getId() == null && customerGroup.getGroupCode() == null) {
                customerInfo1.setCustomerGroup(null);
            } else {
                customerInfo1.setCustomerGroup(customerGroup);
            }
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CRG_ALPHA))) {
            final CreditRiskGradingAlpha creditRiskGradingAlpha = creditRiskGradingAlphaService
                    .save(objectMapper().convertValue(o, CreditRiskGradingAlpha.class));
            customerInfo1.setCreditRiskGradingAlpha(creditRiskGradingAlpha);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CRG))) {
            final CreditRiskGrading creditRiskGrading = creditRiskGradingService
                    .save(objectMapper().convertValue(o, CreditRiskGrading.class));
            customerInfo1.setCreditRiskGrading(creditRiskGrading);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CRG_GAMMA))) {
            final CrgGamma crgGamma = crgGammaService
                    .save(objectMapper().convertValue(o, CrgGamma.class));
            customerInfo1.setCrgGamma(crgGamma);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CICL))) {
            final Cicl cicl = cIclService
                    .save(objectMapper().convertValue(o, Cicl.class));
            customerInfo1.setCicl(cicl);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.INCOME_FROM_ACCOUNT))) {
            final IncomeFromAccount incomeFromAccount = incomeFromAccountServices
                    .save(objectMapper().convertValue(o, IncomeFromAccount.class));
            customerInfo1.setIncomeFromAccount(incomeFromAccount);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.NET_TRADING_ASSETS))) {
            final NetTradingAssets netTradingAssets = netTradingAssetsService
                    .save(objectMapper().convertValue(o, NetTradingAssets.class));
            customerInfo1.setNetTradingAssets(netTradingAssets);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CREDIT_CHECKlIST))) {
            final CreditChecklist creditChecklist = creditChecklistService
                    .save(objectMapper().convertValue(o, CreditChecklist.class));
            customerInfo1.setCreditChecklist(creditChecklist);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.SYNOPSIS_CREDITWORTHINESS))) {
            final SynopsisCreditworthiness synopsisCreditworthiness = synopsisCreditworthinessService
                    .save(objectMapper().convertValue(o, SynopsisCreditworthiness.class));
            customerInfo1.setSynopsisCreditworthiness(synopsisCreditworthiness);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.BORROWER_PORTFOLIO))) {
            final BorrowerPortFolio borrowerPortFolio = borrowerPortFolioService
                    .save(objectMapper().convertValue(o, BorrowerPortFolio.class));
            customerInfo1.setBorrowerPortFolio(borrowerPortFolio);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.BASEL_RISK_EXPOSURE))) {
            final MicroBaselRiskExposure microBaselRiskExposure = microBaselRiskExposureService
                    .save(objectMapper().convertValue(o, MicroBaselRiskExposure.class));
            customerInfo1.setMicroBaselRiskExposure(microBaselRiskExposure);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.MICRO_BORROWER_FINANCIAL))) {
            final MicroBorrowerFinancial microBorrowerFinancial = microBorrowerFinancialService
                    .save(objectMapper().convertValue(o, MicroBorrowerFinancial.class));
            customerInfo1.setMicroBorrowerFinancial(microBorrowerFinancial);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.MICRO_OTHER_PARAMETERS))) {
            final MicroOtherParameters microOtherParameters = microOtherParametersService
                    .save(objectMapper().convertValue(o, MicroOtherParameters.class));
            customerInfo1.setMicroOtherParameters(microOtherParameters);
        }
        else if ((template.equalsIgnoreCase(TemplateNameConstant.MARKETING_ACTIVITIES))) {
            final MarketingActivities marketingActivities = marketingActivitiesService
                    .save(objectMapper().convertValue(o, MarketingActivities.class));
            customerInfo1.setMarketingActivities(marketingActivities);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.CUSTOMER_REPORTING_INFO))) {
            TypeFactory typeFactory = objectMapper().getTypeFactory();
            final List<ReportingInfoLevel> reportingInfoLevels = objectMapper().convertValue(o,
                    typeFactory.constructCollectionType(List.class, ReportingInfoLevel.class));
            customerInfo1.setReportingInfoLevels(reportingInfoLevels);
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.COMMENTS))) {
            try {
                String data = objectMapper().writeValueAsString(o);
                customerInfo1.setData(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ((template.equalsIgnoreCase(TemplateNameConstant.PREVIOUS_SECURITY))) {
            try {
                String data = objectMapper().writeValueAsString(o);
                customerInfo1.setData(data);
            } catch (Exception e) {
                log.error("unable to set data for previous Security {}", e.getMessage());
            }
        }
        customerInfo1.setLoanFlags(loanFlagService.findAllByCustomerInfoId(customerInfoId));
        return customerInfoRepository.save(customerInfo1);
    }

    @Override
    public CustomerInfo findByAssociateIdAndCustomerType(Long id, CustomerType customerType) {
        return customerInfoRepository.findByAssociateIdAndCustomerType(id, customerType);
    }

    @Override
    public CustomerInfo findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(
            CustomerType customerType, String idNumber, String idRegPlace,
            CustomerIdType customerIdType, Date date) {
        return customerInfoRepository
                .findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(customerType, idNumber,
                        idRegPlace, customerIdType, date);
    }

    /**
     * this method is use for cbs group only
     **/
    @Override
    public String updateObligor(String obligor, Long id) {
        customerInfoRepository.updateObligorByCustomerInfoId(obligor, id);
        return null;
    }

    @Override
    public Object updateNepaliConfigData(String nepData, Long id) {
        CustomerInfo customerInfo = customerInfoRepository.getOne(id);
        customerInfo.setNepData(nepData);
        return customerInfoRepository.save(customerInfo);
    }

    @Override
    public CustomerInfo updateCustomerBranch(Long customerInfoId, Long branchId) {
        CustomerInfo customerInfo = customerInfoRepository.getOne(customerInfoId);
        customerInfo.setBranch(branchService.findOne(branchId));
        return customerInfoRepository.save(customerInfo);
    }



    @Override
    protected BaseSpecBuilder<CustomerInfo> getSpec(Map<String, String> filterParams) {
        filterParams.values().removeIf(Objects::isNull);
        filterParams.values().removeIf(value -> value.equals("null") || value.equals("undefined"));
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
        if (filterParams.containsKey("branchIds")) {
            branchAccess = filterParams.get("branchIds");
        }
        filterParams.put("branchIds", branchAccess);
        return new CustomerInfoSpecBuilder(filterParams);
    }


    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new SimpleDateFormat(AppConstant.DATE_FORMAT));
        return objectMapper;
    }

    @Override
    public String title() {
        return "Customer Report";
    }

    @Override
    public List<AbstractColumn> columns() {

        Format format = new Format() {
            @Override
            public StringBuffer format(Object o, StringBuffer stringBuffer,
                                       FieldPosition fieldPosition) {
                return stringBuffer.append(o.toString().toUpperCase());
            }

            @Override
            public Object parseObject(String s, ParsePosition parsePosition) {
                return null;
            }
        };
        AbstractColumn columnBranch = ColumnBuilder.getNew()
                .setColumnProperty("branch.name", String.class.getName())
                .setTitle("Branch").setWidth(100)
                .build();

        AbstractColumn columnName = ColumnBuilder.getNew()
                .setColumnProperty("name", String.class.getName())
                .setTitle("Name").setWidth(100)
                .build();
        AbstractColumn customerType = ColumnBuilder.getNew()
                .setColumnProperty("customerType", CustomerType.class.getName())
                .setTitle("Customer Type").setWidth(100).setTextFormatter(format)
                .build();

        AbstractColumn idType = ColumnBuilder.getNew()
                .setColumnProperty("idType", CustomerIdType.class.getName())
                .setTitle("  ID Type  ").setWidth(100).setTextFormatter(format)
                .build();
        AbstractColumn idNumber = ColumnBuilder.getNew()
                .setColumnProperty("idNumber", String.class.getName())
                .setTitle("ID Number").setWidth(100)
                .build();

        AbstractColumn issuePlace = ColumnBuilder.getNew()
                .setColumnProperty("idRegPlace", String.class.getName())
                .setTitle("ID Issue Place").setWidth(100)
                .build();
        AbstractColumn email = ColumnBuilder.getNew()
                .setColumnProperty("email", String.class.getName())
                .setTitle("Email").setWidth(100)
                .build();
        AbstractColumn contactNumber = ColumnBuilder.getNew()
                .setColumnProperty("contactNo", String.class.getName())
                .setTitle("Contact No.").setWidth(100)
                .build();
        AbstractColumn columnAssociate = ColumnBuilder.getNew()
                .setColumnProperty("createdAt", Date.class.getName())
                .setTitle("Associate Since").setWidth(100).setPattern("dd MMM, YYYY")
                .build();
        return Arrays
                .asList(columnBranch, columnName, customerType, idType, idNumber, issuePlace, email,
                        contactNumber, columnAssociate);
    }

    @Override
    public ReportParam populate(Optional optional) {
        String filePath = new PathBuilder(UploadDir.initialDocument)
                .buildBuildFormDownloadPath("customer");
        Map<String, String> map = objectMapper().convertValue(optional.get(), Map.class);
        Specification<CustomerInfo> specification = getSpec(map).build();
        List customerInfoList = customerInfoRepository.findAll(specification);
        String filterBy = "";
        StringBuilder sb = new StringBuilder();
        map.values().removeIf(Objects::isNull);
        map.entrySet().forEach(a -> {
            String k = a.getKey();
            String v = a.getValue();
            if (!k.equalsIgnoreCase("branchIds")) {
                StringBuilder spaceCamelCase = new StringBuilder();
                String[] camelCase = StringUtils.splitByCharacterTypeCamelCase(k);
                for (String s : camelCase) {
                    spaceCamelCase.append(StringUtils.capitalize(s)).append(" ");
                }
                sb.append(spaceCamelCase).append(" = ").append(v).append(", ");
            }

        });
        if (!ObjectUtils.isEmpty(sb.toString())) {
            filterBy = " Filter By : " + sb.toString().substring(0, sb.toString().length() - 2);
        }
        return ReportParam.builder().reportName("Customer Report").title(title())
                .subTitle(filterBy).columns(columns()).data(customerInfoList)
                .reportType(ReportType.FORM_REPORT)
                .filePath(UploadDir.WINDOWS_PATH + filePath).exportType(ExportType.XLS)
                .build();

    }

    @Override
    public String csv(Object searchDto) {
        Report report = ReportFactory.getReport(populate(Optional.of(searchDto)));
        return new PathBuilder(UploadDir.initialDocument)
                .buildBuildFormDownloadPath("customer") + report.getFileName();

    }

    @Override
    public Page<CustomerListDto> getCustomerListDto(Object searchDto, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(searchDto, Map.class);
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
                .map(Object::toString).collect(Collectors.joining(","));
        if (s.containsKey("branchIds")) {
            branchAccess = s.get("branchIds");
        }
        s.put("branchIds", branchAccess);
        s.values().removeIf(Objects::isNull);
        final CustomerInfoSpecBuilder customerInfoSpecBuilder = new CustomerInfoSpecBuilder(s);
        final Specification<CustomerInfo> specification = customerInfoSpecBuilder.build();
        return getCustomerDtoList(specification, pageable);
    }

    private Page<CustomerListDto> getCustomerDtoList(Specification<CustomerInfo> innerSpec, Pageable pageable){
        CriteriaDto<CustomerInfo, CustomerListDto> criteriaDto = new CriteriaDto<>(
                CustomerInfo.class, CustomerListDto.class, innerSpec, columns,joinColumn
        );

        BaseCriteriaQuery<CustomerInfo, CustomerListDto> baseCriteriaQuery = new BaseCriteriaQuery<>();

        return  baseCriteriaQuery.getListPage(criteriaDto, em, pageable, "createdAt",BaseCriteriaQuery.ASC);

    }

}




