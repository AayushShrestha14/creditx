package com.sb.solutions.core.config.security;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.sb.solutions.core.config.security.property.MailProperties;
import com.sb.solutions.core.config.security.roleAndPermission.RoleAndPermissionDao;

/**
 * @author Rujan Maharjan on 7/4/2019
 */

@Configuration
public class RefreshEmailBean {

    private static final Logger logger = LoggerFactory.getLogger(RefreshEmailBean.class);
    private final RoleAndPermissionDao roleAndPermissionDao;
    private final MailProperties mailProperties;

    @Autowired
    public RefreshEmailBean(
        MailProperties mailProperties,
        RoleAndPermissionDao roleAndPermissionDao) {
        this.mailProperties = mailProperties;
        this.roleAndPermissionDao = roleAndPermissionDao;
    }

    @RefreshScope
    @Bean
    public JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        List<Map<String, Object>> map = roleAndPermissionDao.getEmailConfig();
        try {
            if (!map.isEmpty()) {
                mailSender.setHost(String.valueOf(map.get(0).get("host")));
                mailSender.setPort((Integer.parseInt(map.get(0).get("port").toString())));
                mailSender.setUsername(String.valueOf(map.get(0).get("username")));
                mailSender.setPassword(String.valueOf(map.get(0).get("password")));

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.smtp.host", String.valueOf(map.get(0).get("host")));
                props.put("mail.smtp.socketFactory.port",
                    String.valueOf(map.get(0).get("port")));
                this.mailProperties.getAdditionalHeaders().forEach(props::put);
                return mailSender;

            }
        } catch (Exception e) {
            logger.error("ERROR CONFIGURATION EMAIL SETUP {}", new Date(), e.getLocalizedMessage());
        }
        return null;
    }
}
