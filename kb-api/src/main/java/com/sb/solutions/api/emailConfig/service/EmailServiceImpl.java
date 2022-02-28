package com.sb.solutions.api.emailConfig.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.emailConfig.entity.EmailConfig;
import com.sb.solutions.api.emailConfig.repository.EmailConfigRepository;
import com.sb.solutions.core.utils.email.MailSenderService;

/**
 * @author Rujan Maharjan on 7/1/2019
 */

@Service
public class EmailServiceImpl implements EmailConfigService {

    private final EmailConfigRepository emailConfigRepository;
    private final MailSenderService mailSenderService;

    public EmailServiceImpl(@Autowired EmailConfigRepository emailConfigRepository,
        @Autowired MailSenderService mailSenderService) {
        this.emailConfigRepository = emailConfigRepository;
        this.mailSenderService = mailSenderService;
    }

    @Override
    public List<EmailConfig> findAll() {
        return emailConfigRepository.findAll();
    }

    @Override
    public EmailConfig findOne(Long id) {
        return emailConfigRepository.getOne(id);
    }

    @Override
    public EmailConfig save(EmailConfig emailConfig) {
        List<EmailConfig> emailConfig1 = emailConfigRepository.findAll();
        if (!emailConfig1.isEmpty()) {
            emailConfig1.get(0).setDomain(emailConfig.getDomain());
            emailConfig1.get(0).setHost(emailConfig.getHost());
            emailConfig1.get(0).setUsername(emailConfig.getUsername());
            emailConfig1.get(0).setPassword(emailConfig.getPassword());
            emailConfig1.get(0).setPort(emailConfig.getPort());
            return emailConfigRepository.save(emailConfig1.get(0));
        }
        return emailConfigRepository.save(emailConfig);
    }

    @Override
    public Page<EmailConfig> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<EmailConfig> saveAll(List<EmailConfig> list) {
        return emailConfigRepository.saveAll(list);
    }
}
