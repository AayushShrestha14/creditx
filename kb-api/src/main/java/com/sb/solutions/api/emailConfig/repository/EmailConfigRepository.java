package com.sb.solutions.api.emailConfig.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sb.solutions.api.emailConfig.entity.EmailConfig;

/**
 * @author Rujan Maharjan on 7/1/2019
 */
public interface EmailConfigRepository extends JpaRepository<EmailConfig, Long> {

}
