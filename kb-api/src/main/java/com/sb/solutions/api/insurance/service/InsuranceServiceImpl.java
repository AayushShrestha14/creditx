package com.sb.solutions.api.insurance.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.service.RoleService;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.repository.CustomerInfoRepository;
import com.sb.solutions.api.helper.HelperDto;
import com.sb.solutions.api.helper.HelperIdType;
import com.sb.solutions.api.insurance.entity.Insurance;
import com.sb.solutions.api.insurance.repository.InsuranceRepository;
import com.sb.solutions.api.insurance.repository.spec.InsuranceSpecBuilder;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.repository.CustomerLoanRepository;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpec;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpecBuilder;
import com.sb.solutions.api.loanflag.entity.CustomerLoanFlag;
import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.api.preference.notificationMaster.repository.spec.NotificationMasterSpec;
import com.sb.solutions.api.preference.notificationMaster.service.NotificationMasterService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.EmailConstant.Template;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanFlag;
import com.sb.solutions.core.enums.NotificationMasterType;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.repository.BaseSpecBuilder;
import com.sb.solutions.core.service.BaseServiceImpl;
import com.sb.solutions.core.utils.email.Email;
import com.sb.solutions.core.utils.email.MailThreadService;
import com.sb.solutions.core.utils.email.dto.InsuranceEmailDto;

@Service("insuranceService")
public class InsuranceServiceImpl extends BaseServiceImpl<Insurance, Long> implements
    InsuranceService {

    @Value("${bank.name}")
    private String bankName;

    @Value("${bank.affiliateId}")
    private String affiliateId;

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceServiceImpl.class);

    private final InsuranceRepository repository;
    private final NotificationMasterService notificationMasterService;
    private final CustomerLoanRepository customerLoanRepository;
    private final CustomerLoanFlagService customerLoanFlagService;
    private final MailThreadService mailThreadService;
    private final UserService userService;
    private final CustomerInfoRepository customerInfoRepository;
    private final RoleService roleService;

    protected InsuranceServiceImpl(
        InsuranceRepository repository,
        NotificationMasterService notificationMasterService,
        CustomerLoanRepository customerLoanRepository,
        CustomerLoanFlagService customerLoanFlagService,
        MailThreadService mailThreadService,
        UserService userService,
        CustomerInfoRepository customerInfoRepository,
        RoleService roleService) {
        super(repository);

        this.repository = repository;
        this.notificationMasterService = notificationMasterService;
        this.customerLoanRepository = customerLoanRepository;
        this.customerLoanFlagService = customerLoanFlagService;
        this.mailThreadService = mailThreadService;
        this.userService = userService;
        this.customerInfoRepository = customerInfoRepository;
        this.roleService = roleService;
    }


    @Override
    protected BaseSpecBuilder<Insurance> getSpec(Map<String, String> filterParams) {
        return new InsuranceSpecBuilder(filterParams);
    }

    @Override
    public void execute(Optional<HelperDto<Long>> helperDto) {
        //TODO disable insurace expiry
//        Map<String, String> insuranceFilter = new HashMap<>();
//        insuranceFilter.put(NotificationMasterSpec.FILTER_BY_NOTIFICATION_KEY,
//            NotificationMasterType.INSURANCE_EXPIRY_NOTIFY.toString());
//        Optional<NotificationMaster> notificationMaster = notificationMasterService
//            .findOneBySpec(insuranceFilter);
//
//        if (!notificationMaster.isPresent()) {
//            LOGGER.error("Insurance Expiry Notification is not configured");
//            return;
//        }
//
//        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());
//        int daysToExpire = notificationMaster.get().getValue();
//        c.add(Calendar.DAY_OF_MONTH, daysToExpire);
//
//        List<CustomerInfo> loanHolders = new ArrayList<>();
//        if (helperDto.isPresent()) {
//            if (helperDto.get().getIdType().equals(HelperIdType.CUSTOMER_INFO)) {
//                // insurance under customer info
//                loanHolders = Collections
//                    .singletonList(customerInfoRepository.getOne(helperDto.get().getId()));
//            } else if (helperDto.get().getIdType().equals(HelperIdType.LOAN)) {
//                // insurance under loan
//                loanHolders = Collections.singletonList(
//                    customerLoanRepository.getOne(helperDto.get().getId()).getLoanHolder());
//            }
//        } else {
//            // insurance under all loans having insurance template
//            loanHolders = customerLoanRepository.findAll(getInsuranceSpecs())
//                .stream()
//                .map(CustomerLoan::getLoanHolder)
//                .collect(Collectors.toList());
//        }
//        for (CustomerInfo holder : loanHolders) {
//            try {
//                CustomerLoanFlag customerLoanFlag = holder.getLoanFlags()
//                    .stream()
//                    .filter(loanFlag -> loanFlag.getFlag().equals(LoanFlag.INSURANCE_EXPIRY))
//                    .collect(CustomerLoanFlag.toSingleton());
//                List<Insurance> expiredInsurance = holder.getInsurance()
//                    .stream()
//                    .filter(i -> i.getExpiryDate().compareTo(c.getTime()) <= 0)
//                    .collect(Collectors.toList());
//                boolean flag = !expiredInsurance.isEmpty();
//                if (flag && customerLoanFlag == null) {
//                    customerLoanFlag = new CustomerLoanFlag();
//                    customerLoanFlag.setFlag(LoanFlag.INSURANCE_EXPIRY);
//                    customerLoanFlag.setCustomerInfo(holder);
//                    customerLoanFlag.setDescription(LoanFlag.INSURANCE_EXPIRY.getValue()[1]);
//                    customerLoanFlag.setOrder(
//                        Integer.parseInt(LoanFlag.INSURANCE_EXPIRY.getValue()[0]));
//                    CustomerLoanFlag savedFlag = customerLoanFlagService.save(customerLoanFlag);
//                    sendInsuranceExpiryEmail(holder, expiredInsurance, savedFlag);     // send mail
//                    return;
//                } else if (!flag && customerLoanFlag != null) {
//                    customerLoanFlagService.deleteById(customerLoanFlag.getId());
//                } else if (flag && customerLoanFlag.getFlag() != null
//                    && (customerLoanFlag.getNotifiedByEmail() == null
//                    || customerLoanFlag.getNotifiedByEmail().equals(Boolean.FALSE))) {
//                    sendInsuranceExpiryEmail(holder, expiredInsurance,
//                        customerLoanFlag);     // send mail
//                }
//            } catch (NullPointerException e) {
//                LOGGER.error("Error updating insurance expiry flag {}", e.getMessage());
//            }
//        }
    }

    private void sendInsuranceExpiryEmail(CustomerInfo holder, List<Insurance> expiredInsurance,
        CustomerLoanFlag flag) {
        final Email emailMaker = new Email();
        emailMaker.setBankName(this.bankName);
        emailMaker.setAffiliateId(this.affiliateId);

        Specification<CustomerLoan> specs = getInsuranceSpecs();
        Map<String, String> t = new HashMap<>();
        t.put(CustomerLoanSpec.FILTER_BY_LOAN_HOLDER_ID, String.valueOf(holder.getId()));
        List<CustomerLoan> loans = customerLoanRepository
            .findAll(specs.and(new CustomerLoanSpecBuilder(t).build()));

        loans.forEach(loan -> {
            // send to loan makers
            List<Role> makerRoles = roleService
                .getByRoleTypeAndStatus(RoleType.MAKER, Status.ACTIVE);
            List<User> makers = new ArrayList<>();
            for (Role makerRole : makerRoles) {
                makers.addAll(userService.findByRoleIdAndBranch(makerRole.getId(),
                    Collections.singletonList(loan.getBranch().getId())));
            }
            User userMaker = userService.getAuthenticatedUser();
            makers = makers.stream().filter(u -> !u.getId().equals(userMaker.getId()))
                .collect(Collectors.toList());

            emailMaker.setTo(userMaker.getEmail());
            emailMaker.setCc(makers.stream().map(User::getEmail).collect(Collectors.toList()));
            emailMaker.setToName(userMaker.getName());
            emailMaker.setName(loan.getCustomerInfo().getCustomerName());
            emailMaker.setInsurances(
                expiredInsurance.stream()
                    .map(i -> new InsuranceEmailDto(i.getCompany(), i.getExpiryDate()))
                    .collect(Collectors.toList())
            );
            emailMaker.setEmail(loan.getCustomerInfo().getEmail());
            emailMaker.setPhoneNumber(loan.getCustomerInfo().getContactNumber());
            emailMaker.setBankBranch(loan.getBranch().getName());
            Template templateMaker = Template.INSURANCE_EXPIRY_MAKER;
            templateMaker.setSubject("Insurance Expiry Notice: " + emailMaker.getName());
            mailThreadService.sendComplexMail(templateMaker, emailMaker);

            // send to the client
            if (loan.getCustomerInfo().getEmail() != null) {
                final Email emailClient = new Email();
                BeanUtils.copyProperties(emailMaker, emailClient);
                emailClient.setToName(loan.getCustomerInfo().getCustomerName());
                emailClient.setTo(loan.getCustomerInfo().getEmail());
                emailClient.setEmail(userMaker.getEmail());
                emailClient.setPhoneNumber(loan.getBranch().getLandlineNumber());
                Template templateClient = Template.INSURANCE_EXPIRY_CLIENT;
                templateClient.setSubject(String.format("Insurance Expiry Notice at %s", bankName));
                mailThreadService.sendMain(templateClient, emailClient);
            }
            customerLoanFlagService.updateEmailStatus(true, flag.getId());
        });
    }

    private Specification<CustomerLoan> getInsuranceSpecs() {
        Map<String, String> t = new HashMap<>();
        t.put(CustomerLoanSpec.FILTER_BY_HAS_INSURANCE, Boolean.toString(true));
        t.put(CustomerLoanSpec.FILTER_BY_IS_CLOSE_RENEW, Boolean.toString(false));
        CustomerLoanSpecBuilder builder = new CustomerLoanSpecBuilder(t);
        return builder.build();
    }
}
