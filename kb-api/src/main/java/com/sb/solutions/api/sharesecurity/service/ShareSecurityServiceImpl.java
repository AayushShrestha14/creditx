package com.sb.solutions.api.sharesecurity.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperIdType;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.repository.CustomerLoanRepository;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpec;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpecBuilder;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.api.nepseCompany.entity.CustomerShareData;
import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.api.nepseCompany.entity.NepseMaster;
import com.sb.solutions.api.nepseCompany.repository.NepseCompanyRepository;
import com.sb.solutions.api.nepseCompany.repository.NepseMasterRepository;
import com.sb.solutions.api.sharesecurity.ShareSecurity;
import com.sb.solutions.api.sharesecurity.repository.ShareSecurityRepo;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanFlag;
import com.sb.solutions.core.enums.ShareType;
import com.sb.solutions.core.enums.Status;

@Service("shareSecurityService")
public class ShareSecurityServiceImpl implements ShareSecurityService {

    private static final Logger logger = LoggerFactory.getLogger(ShareSecurityService.class);
    private final CustomerLoanFlagService customerLoanFlagService;
    private final NepseMasterRepository nepseMasterRepository;
    private final NepseCompanyRepository nepseCompanyRepository;
    private final CustomerLoanRepository customerLoanRepository;
    private final ShareSecurityRepo shareSecurityRepository;
    private final LoanConfigService loanConfigService;

    public ShareSecurityServiceImpl(
        ShareSecurityRepo shareSecurityRepository,
        NepseMasterRepository nepseMasterRepository,
        NepseCompanyRepository nepseCompanyRepository,
        CustomerLoanRepository customerLoanRepository,
        CustomerLoanFlagService customerLoanFlagService,
        LoanConfigService loanConfigService) {
        this.shareSecurityRepository = shareSecurityRepository;
        this.nepseMasterRepository = nepseMasterRepository;
        this.nepseCompanyRepository = nepseCompanyRepository;
        this.customerLoanRepository = customerLoanRepository;
        this.customerLoanFlagService = customerLoanFlagService;
        this.loanConfigService = loanConfigService;
    }

    @Override
    public List<ShareSecurity> findAll() {
        return shareSecurityRepository.findAll();
    }

    @Override
    public ShareSecurity findOne(Long id) {
        return shareSecurityRepository.getOne(id);
    }

    @Override
    public ShareSecurity save(ShareSecurity shareSecurity) {
        return shareSecurityRepository.save(shareSecurity);
    }

    @Override
    public Page<ShareSecurity> findAllPageable(Object t, Pageable pageable) {
        return shareSecurityRepository.findAll(pageable);
    }

    @Override
    public void execute(Optional<HelperDto<Long>> helperDto) {
        final NepseMaster master = nepseMasterRepository.findByStatus(Status.ACTIVE);
        final Map<ShareType, Double> masterMap = new HashMap<>();
        masterMap.put(ShareType.ORDINARY, master.getOrdinary() / 100);
        masterMap.put(ShareType.PROMOTER, master.getPromoter() / 100);
        final Map<String, Double> marketPriceMap = nepseCompanyRepository.findAll().stream()
            .collect(
                Collectors.toMap(NepseCompany::getCompanyCode, NepseCompany::getAmountPerUnit));
        List<CustomerLoan> loan = new ArrayList<>();
        if (helperDto.isPresent()) {
            // share under customer info loans having share template
            if (helperDto.get().getIdType().equals(HelperIdType.CUSTOMER_INFO)) {
                loan = getLoanHavingShareTemplate(helperDto.get().getId());
            } else if (helperDto.get().getIdType().equals(HelperIdType.LOAN)) {
                // share template under customer info
                loan = Collections
                    .singletonList(customerLoanRepository.getOne(helperDto.get().getId()));
            }
        } else {
            // share under all loans having share template
            loan = this.getLoanHavingShareTemplate();
        }
        loan.stream()
            .filter(customerLoan -> (null != customerLoan.getProposal().getProposedLimit()
                && customerLoan.getProposal().getProposedLimit().compareTo(BigDecimal.ZERO)
                > 0))
            .forEach(customerLoan -> {
                CustomerLoanFlag customerLoanFlag = customerLoan.getLoanHolder().getLoanFlags()
                    .stream()
                    .filter(loanFlag -> (
                        loanFlag.getFlag().equals(LoanFlag.INSUFFICIENT_SHARE_AMOUNT)
                            && loanFlag.getCustomerLoanId().equals(customerLoan.getId()))).collect(CustomerLoanFlag.toSingleton());

                CustomerLoanFlag fixedLoanFlag = customerLoan.getLoanHolder().getLoanFlags()
                    .stream()
                    .filter(loanFlag -> (
                        loanFlag.getFlag().equals(LoanFlag.INSUFFICIENT_Fixed_AMOUNT)
                            && loanFlag.getCustomerLoanId().equals(customerLoan.getId()))).collect(CustomerLoanFlag.toSingleton());

                CustomerLoanFlag vehicleLoanFlag = customerLoan.getLoanHolder().getLoanFlags()
                    .stream()
                    .filter(loanFlag -> (
                        loanFlag.getFlag().equals(LoanFlag.INSUFFICIENT_VEHICLE_AMOUNT)
                            && loanFlag.getCustomerLoanId().equals(customerLoan.getId()))).collect(CustomerLoanFlag.toSingleton());


                LoanConfig loanConfigDetail = loanConfigService.findOne(customerLoan.getLoan().getId());
                switch (loanConfigDetail.getLoanTag().name()) {
                    case "GENERAL":
                        customerLoanFlagService
                            .deleteCustomerLoanFlagById(
                                !ObjectUtils.isEmpty(fixedLoanFlag) ? fixedLoanFlag.getId() : null);
                        customerLoanFlagService
                            .deleteCustomerLoanFlagById(
                                !ObjectUtils.isEmpty(customerLoanFlag) ? customerLoanFlag.getId() : null);
                        customerLoanFlagService
                            .deleteCustomerLoanFlagById(
                                !ObjectUtils.isEmpty(vehicleLoanFlag) ? vehicleLoanFlag.getId() : null);
                        break;
                    case "VEHICLE":
                        customerLoanFlagService
                            .deleteCustomerLoanFlagById(
                                !ObjectUtils.isEmpty(fixedLoanFlag) ? fixedLoanFlag.getId() : null);
                        customerLoanFlagService
                            .deleteCustomerLoanFlagById(
                                !ObjectUtils.isEmpty(customerLoanFlag) ? customerLoanFlag.getId() : null);
                        break;
                    case "SHARE_SECURITY":
                        logger.info("Customer loan id {} ", customerLoan.getId());
                        ShareSecurity shareSecurity = customerLoan.getLoanHolder().getShareSecurity();
                        if (null != shareSecurity) {
                            List<CustomerShareData> shareDataList = shareSecurity.getCustomerShareData();
                            AtomicReference<BigDecimal> reCalculateAmount = new AtomicReference<>(
                                BigDecimal.ZERO);
                            shareDataList.forEach(customerShareData -> {

                                String companyCode = customerShareData.getCompanyCode();
                                if (marketPriceMap.containsKey(companyCode)) {
                                    BigDecimal newValue = reCalculateAmount.get()
                                        .add(BigDecimal.valueOf(customerShareData.getAmountPerUnit())
                                            .multiply(
                                                BigDecimal.valueOf(customerShareData.getTotalShareUnit()))
                                            .multiply(BigDecimal
                                                .valueOf(masterMap.get(customerShareData.getShareType()))));
                                    reCalculateAmount.set(newValue);

                                }
                            });
                            logger.info("Recalculate amount {} ===  {} proposal limit",
                                reCalculateAmount.get(), customerLoan.getProposal().getProposedLimit());


                            boolean flag = customerLoan.getProposal().getProposedLimit()
                                .compareTo(reCalculateAmount.get()) >= 1;
                            if (flag && customerLoanFlag == null) {
                                customerLoanFlag = new CustomerLoanFlag();
                                customerLoanFlag.setCustomerInfo(customerLoan.getLoanHolder());
                                customerLoanFlag.setCustomerLoanId(customerLoan.getId());
                                customerLoanFlag.setFlag(LoanFlag.INSUFFICIENT_SHARE_AMOUNT);
                                customerLoanFlag.setDescription(String
                                    .format(LoanFlag.INSUFFICIENT_SHARE_AMOUNT.getValue()[1],
                                        reCalculateAmount.get()));
                                customerLoanFlag.setOrder(
                                    Integer.parseInt(LoanFlag.INSUFFICIENT_SHARE_AMOUNT.getValue()[0]));
                                customerLoanFlagService.save(customerLoanFlag);
                            } else if (!flag && customerLoanFlag != null) {
                                customerLoanFlagService
                                    .deleteCustomerLoanFlagById(customerLoanFlag.getId());
                            }
                        }
                        if (fixedLoanFlag != null) {
                            customerLoanFlagService
                                .deleteCustomerLoanFlagById(fixedLoanFlag.getId());
                        }
                        if (vehicleLoanFlag != null) {
                            customerLoanFlagService
                                .deleteCustomerLoanFlagById(vehicleLoanFlag.getId());
                        }
                        break;
                }

            });
    }

    @Override
    public List<ShareSecurity> saveAll(List<ShareSecurity> list) {
        return shareSecurityRepository.saveAll(list);
    }

    private List<CustomerLoan> getLoanHavingShareTemplate() {
        return customerLoanRepository.findAll(getLoanHavingShareSpecs());
    }

    private List<CustomerLoan> getLoanHavingShareTemplate(Long customerInfoId) {
        Specification<CustomerLoan> specs = getLoanHavingShareSpecs();
        Map<String, String> filter = new HashMap<>();
        filter.put(CustomerLoanSpec.FILTER_BY_LOAN_HOLDER_ID, String.valueOf(customerInfoId));
        return customerLoanRepository
            .findAll(specs.and(new CustomerLoanSpecBuilder(filter).build()));
    }

    private Specification<CustomerLoan> getLoanHavingShareSpecs() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate nonClosedLoanFilter = criteriaBuilder
                .and(criteriaBuilder.notEqual(root.get("documentStatus"), DocStatus.CLOSED));
            Predicate nonRejectedLoanFilter = criteriaBuilder
                .and(criteriaBuilder.notEqual(root.get("documentStatus"),
                    DocStatus.REJECTED));
//            Predicate predicateForShareTemplate = criteriaBuilder
//                .isMember(AppConstant.TEMPLATE_SHARE_SECURITY,
//                    root.join("loan").get("templateList"));
            return criteriaBuilder
                .and(nonClosedLoanFilter, nonRejectedLoanFilter);
        };
    }
}
