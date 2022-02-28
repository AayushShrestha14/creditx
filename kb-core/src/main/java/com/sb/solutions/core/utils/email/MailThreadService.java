package com.sb.solutions.core.utils.email;

import java.io.IOException;
import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sb.solutions.core.constant.EmailConstant;
import com.sb.solutions.core.exception.ServiceValidationException;

@Service
public class MailThreadService {

    private final Logger logger = LoggerFactory.getLogger(MailThreadService.class);

    private final MailSenderService mailSenderService;

    @Value("${bank.name}")
    private String bankName;

    @Value("${bank.website}")
    private String bankWebsite;

    @Value("${bank.affiliateId}")
    private String affiliateId;


    public MailThreadService(@Autowired MailSenderService mailSenderService) {

        this.mailSenderService = mailSenderService;
    }

    public void sendMain(EmailConstant.Template template, Email email) {
        new Thread(() -> {
            try {
                mailSenderService.send(template, email);
            } catch (Exception e) {
                logger.error("error sending email", e);
            }
        }).start();
    }

    public void sendComplexMail(EmailConstant.Template template, Email email) {
        new Thread(() -> {
            try {
                mailSenderService.sendComplexMail(template, email);
            } catch (Exception e) {
                logger.error("error sending email", e);
            }
        }).start();
    }

    public void testMail(EmailConstant.Template template, Email email) {
        email.setBankName(bankName);
        email.setBankWebsite(bankWebsite);
        email.setAffiliateId(affiliateId);
        mailSenderService.send(template, email);
    }
}
