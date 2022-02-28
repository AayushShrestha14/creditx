package com.sb.solutions.api.companyInfo.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.companyInfo.model.repository.CompanyInfoRepository;
import com.sb.solutions.api.companyInfo.model.repository.specification.CompanyInfoSpecBuilder;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.enums.CustomerIdType;
import com.sb.solutions.api.customer.enums.CustomerType;
import com.sb.solutions.api.customer.service.CustomerInfoService;
import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.api.preference.notificationMaster.service.NotificationMasterService;
import com.sb.solutions.core.exception.ServiceValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CompanyInfoServiceImpl implements CompanyInfoService {

    private final CompanyInfoRepository companyInfoRepository;
    private final CustomerInfoService customerInfoService;
    private final NotificationMasterService notificationMasterService;
    private final CustomerLoanFlagService customerLoanFlagService;

    public CompanyInfoServiceImpl(
        @Autowired CompanyInfoRepository companyInfoRepository,
        CustomerInfoService customerInfoService,
        NotificationMasterService notificationMasterService,
        CustomerLoanFlagService customerLoanFlagService
    ) {
        this.companyInfoRepository = companyInfoRepository;
        this.customerInfoService = customerInfoService;
        this.notificationMasterService = notificationMasterService;
        this.customerLoanFlagService = customerLoanFlagService;
    }

    @Override
    public List<CompanyInfo> findAll() {
        return companyInfoRepository.findAll();
    }

    @Override
    public CompanyInfo findOne(Long id) {
        return companyInfoRepository.getOne(id);
    }

    @Transactional
    @Override
    public CompanyInfo save(CompanyInfo companyInfo) {
        if (!companyInfo.isValid()) {
            throw new ServiceValidationException(
                companyInfo.getValidationMsg());
        }
        if (ObjectUtils.isEmpty(companyInfo.getId())) {
            CustomerInfo isExist = customerInfoService
                .findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(
                    CustomerType.INSTITUTION, companyInfo.getPanNumber(),
                    companyInfo.getIssuePlace(),
                    CustomerIdType.PAN, companyInfo.getLegalStatus().getRegistrationDate());
            if (!ObjectUtils.isEmpty(isExist)) {
                Branch branch = isExist.getBranch();
                throw new ServiceValidationException(
                    "Customer Exist!" + "This Customer is associate with branch " + branch
                        .getName());
            }

            if (!ObjectUtils.isEmpty(companyInfo.getPanNumber())) {
                CompanyInfo samePanNumber = companyInfoRepository
                    .findCompanyInfoByPanNumber(companyInfo.getPanNumber());
                if (samePanNumber != null) {
                    throw new ServiceValidationException(
                        "The pan number is already in use. You cannot create two clients with same pan number");
                }
            }
        }

        final CompanyInfo info = companyInfoRepository.save(companyInfo);
        info.setBankingRelationship(companyInfo.getBankingRelationship());
        info.setCustomerCode(companyInfo.getCustomerCode());
        info.setClientType(companyInfo.getClientType());
        info.setSubsectorDetail(companyInfo.getSubsectorDetail());
        CustomerInfo customerInfo = customerInfoService.saveObject(info);
        execute(customerInfo.getId());
        return info;
    }

    @Override
    public Page<CompanyInfo> findAllPageable(Object t, Pageable pageable) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> s = objectMapper.convertValue(t, Map.class);
        final CompanyInfoSpecBuilder companyInfoSpecBuilder = new CompanyInfoSpecBuilder(s);
        Specification<CompanyInfo> specification = companyInfoSpecBuilder.build();
        return companyInfoRepository.findAll(specification, pageable);
    }

    @Override
    public List<CompanyInfo> saveAll(List<CompanyInfo> list) {
        return companyInfoRepository.saveAll(list);
    }

    @Override
    public CompanyInfo findCompanyInfoByRegistrationNumber(String registrationNumber) {
        return companyInfoRepository.findCompanyInfoByRegistrationNumber(registrationNumber);
    }

    @Override
    public CompanyInfo findCompanyInfoByPanNumber(String panNumber) {
        return companyInfoRepository.findCompanyInfoByPanNumber(panNumber);
    }

    @Override
    public void execute(Long customerInfoId) {
        //TODO disable pan vat
//        Optional<CustomerInfo> customerInfo = customerInfoService.findOne(customerInfoId);
//        if (!customerInfo.isPresent()) {
//            log.error(
//                "Error checking company VAT/PAN expiry. Customer Info with id {} not found",
//                customerInfoId);
//            return;
//        }
//
//        Map<String, String> companyNotificationFilter = new HashMap<>();
//        companyNotificationFilter.put(
//            NotificationMasterSpec.FILTER_BY_NOTIFICATION_KEY,
//            NotificationMasterType.COMPANY_REGISTRATION_EXPIRY_BEFORE.toString()
//        );
//        Optional<NotificationMaster> notificationMaster = notificationMasterService
//            .findOneBySpec(companyNotificationFilter);
//        if (!notificationMaster.isPresent()) {
//            log.error("Company VAT/PAN Expiry Notification not configured");
//            return;
//        }
//
////        CustomerLoanFlag customerLoanFlag = customerInfo.get().getLoanFlags()
////            .stream()
////            .filter(loanFlag -> loanFlag.getFlag().equals(LoanFlag.COMPANY_VAT_PAN_EXPIRY))
////            .collect(CustomerLoanFlag.toSingleton());
////
////        CustomerLoanFlag customerEmptyLoanFlag = customerInfo.get().getLoanFlags()
////            .stream()
////            .filter(loanFlag -> loanFlag.getFlag().equals(LoanFlag.EMPTY_COMPANY_VAT_PAN_EXPIRY))
////            .collect(CustomerLoanFlag.toSingleton());
//
//
//        try {
//            if (!ObjectUtils
//                .isEmpty(this.findOne(customerInfo.get().getAssociateId()).getLegalStatus()
//                    .getRegistrationExpiryDate())) {
//            int daysToExpiryBefore = notificationMaster.get().getValue();
//            Calendar c = Calendar.getInstance();
//            c.setTime(new Date());
//            c.add(Calendar.DAY_OF_MONTH, daysToExpiryBefore);
//            SimpleDateFormat dateFormat = new SimpleDateFormat(AppConstant.MM_DD_YYYY);
//            Date today = dateFormat.parse(dateFormat.format(c.getTime()));
//            Date expiry = dateFormat.parse(dateFormat.format(
//                this.findOne(customerInfo.get().getAssociateId()).getLegalStatus()
//                    .getRegistrationExpiryDate()));
//            boolean flag = expiry.before(today);
////            if (flag && customerLoanFlag == null) {
////                customerLoanFlag = new CustomerLoanFlag();
////                customerLoanFlag.setCustomerInfo(customerInfo.get());
////                customerLoanFlag.setFlag(LoanFlag.COMPANY_VAT_PAN_EXPIRY);
////                customerLoanFlag.setDescription(
////                    String.format(LoanFlag.COMPANY_VAT_PAN_EXPIRY.getValue()[1],
////                        daysToExpiryBefore));
////                customerLoanFlag.setOrder(
////                    Integer.parseInt(LoanFlag.COMPANY_VAT_PAN_EXPIRY.getValue()[0]));
////                customerLoanFlagService.save(customerLoanFlag);
////            }
////            else if (!flag && customerLoanFlag != null) {
////                customerLoanFlagService
////                    .deleteCustomerLoanFlagById(customerLoanFlag.getId());
////            }
////            if (customerEmptyLoanFlag != null) {
////                customerLoanFlagService
////                    .deleteCustomerLoanFlagById(customerEmptyLoanFlag.getId());
////            }
//        } else {
//                if (customerLoanFlag != null) {
//                customerLoanFlagService
//                    .deleteCustomerLoanFlagById(customerLoanFlag.getId());
//                }
//                if (customerEmptyLoanFlag != null) {
//                    customerLoanFlagService
//                        .deleteCustomerLoanFlagById(customerEmptyLoanFlag.getId());
//                }
//                customerLoanFlag = new CustomerLoanFlag();
//                customerLoanFlag.setCustomerInfo(customerInfo.get());
//                customerLoanFlag.setFlag(LoanFlag.EMPTY_COMPANY_VAT_PAN_EXPIRY);
//                customerLoanFlag.setDescription(
//                    String.format(LoanFlag.EMPTY_COMPANY_VAT_PAN_EXPIRY.getValue()[1]));
//                customerLoanFlag.setOrder(
//                    Integer.parseInt(LoanFlag.EMPTY_COMPANY_VAT_PAN_EXPIRY.getValue()[0]));
//                customerLoanFlagService.save(customerLoanFlag);
//
//            }
//        } catch (ParseException e) {
//            log.error("Error parsing company registration expiry date");
//        }
    }
}
