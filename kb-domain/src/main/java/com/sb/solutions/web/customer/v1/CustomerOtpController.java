package com.sb.solutions.web.customer.v1;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.customerOtp.entity.CustomerOtp;
import com.sb.solutions.api.customerOtp.service.CustomerOtpService;
import com.sb.solutions.core.constant.EmailConstant.Template;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.date.DateManipulator;
import com.sb.solutions.core.utils.email.Email;
import com.sb.solutions.core.utils.email.MailSenderService;
import com.sb.solutions.core.utils.string.StringUtil;
import com.sb.solutions.web.customer.v1.dto.CustomerOtpDto;
import com.sb.solutions.web.customer.v1.dto.CustomerOtpTokenDto;
import com.sb.solutions.web.customer.v1.mapper.CustomerOtpMapper;
import com.sb.solutions.web.customer.v1.mapper.CustomerOtpTokenMapper;

@RestController
@RequestMapping("/v1/customer-otp")
public class CustomerOtpController {

    private final CustomerOtpService customerOtpService;
    private final CustomerOtpMapper customerOtpMapper;
    private final CustomerOtpTokenMapper customerOtpTokenMapper;
    private final MailSenderService mailSenderService;
    private final Logger logger = LoggerFactory.getLogger(CustomerOtpController.class);

    @Value("${bank.name}")
    private String bankName;

    @Value("${bank.affiliateId}")
    private String affiliateId;

    public CustomerOtpController(
        @Autowired CustomerOtpService customerOtpService,
        @Autowired CustomerOtpMapper customerOtpMapper,
        @Autowired CustomerOtpTokenMapper customerOtpTokenMapper,
        @Autowired MailSenderService mailSenderService
    ) {
        this.customerOtpService = customerOtpService;
        this.customerOtpMapper = customerOtpMapper;
        this.customerOtpTokenMapper = customerOtpTokenMapper;
        this.mailSenderService = mailSenderService;
    }

    @PostMapping
    public ResponseEntity<?> generateOTP(@RequestBody CustomerOtpDto customerOtpDto) {
        CustomerOtp customerOtp = customerOtpService
            .findByEmailOrMobile(customerOtpDto.getEmail(), customerOtpDto.getMobile());
        if (customerOtp == null) {
            customerOtp = customerOtpMapper.mapDtoToEntity(customerOtpDto);
        }
        DateManipulator dateManipulator = new DateManipulator(new Date());
        customerOtp.setOtp(StringUtil.generateNumber(4));
        customerOtp.setExpiry(dateManipulator.addMinutes(5));
        final CustomerOtp savedCustomerOtp = customerOtpService.save(customerOtp);
        logger.debug("One Time Password (OTP) generated. {}", savedCustomerOtp);
        sendOtpMail(customerOtpDto, savedCustomerOtp.getOtp());
        return new RestResponseDto()
            .successModel(customerOtpMapper.mapEntityToDto(savedCustomerOtp));
    }

    @PostMapping(value = "/verify")
    public ResponseEntity<?> verifyOTP(@RequestBody CustomerOtpTokenDto customerOtpTokenDto) {
        CustomerOtp customerOtp = customerOtpService.findOne(customerOtpTokenDto.getId());
        if (!customerOtp.getOtp().equals(customerOtpTokenDto.getOtp())) {
            logger.debug("OTP Token didn't match");
            return new RestResponseDto().failureModel("Token didn't match");
        } else if (customerOtp.getExpiry().before(new Date())) {
            logger.debug("OTP Token expired");
            return new RestResponseDto().failureModel("One Time Password has expired.");
        } else {
            customerOtpService.delete(customerOtpTokenMapper.mapDtoToEntity(customerOtpTokenDto));
            return new RestResponseDto().successModel("Access Granted");
        }
    }

    @PostMapping(value = "/regenerate")
    public ResponseEntity<?> regenerateOTP(@RequestBody CustomerOtpDto customerOtpDto) {
        CustomerOtp updateOtp = customerOtpService.findOne(customerOtpDto.getId());
        DateManipulator dateManipulator = new DateManipulator(new Date());
        updateOtp.setOtp(StringUtil.generateNumber(4));
        updateOtp.setExpiry(dateManipulator.addMinutes(5));
        final CustomerOtp customerOtp = customerOtpService.save(updateOtp);
        logger.debug("One Time Password (OTP) regenerated {}", updateOtp);
        sendOtpMail(customerOtpDto, customerOtp.getOtp());
        return new RestResponseDto().successModel(customerOtpMapper.mapEntityToDto(customerOtp));
    }

    private void sendOtpMail(CustomerOtpDto customerOtpDto, String otp) {
        Email email = new Email();
        email.setTo(customerOtpDto.getEmail());
        email.setToName(customerOtpDto.getFirstName() + ' ' + customerOtpDto.getLastName());
        email.setBankName(this.bankName);
        email.setAffiliateId(this.affiliateId);
        email.setBody(otp);
        mailSenderService.send(Template.ONE_TIME_PASSWORD, email);
        logger.debug("Email sent including One Time Password (OTP).");
    }

}
