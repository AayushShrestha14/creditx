package com.sb.solutions.api.emailConfig.entity;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author Rujan Maharjan on 2/13/2019
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class EmailConfig extends BaseEntity<Long> {


    @NotNull(message = "Name should not be null")
    private String username;

    @NotNull(message = "Email should not be null")
    private String password;

    @NotNull(message = "Host should not be null")
    private String host;

    @NotNull(message = "Domain should not be null")
    private String domain;

    @NotNull(message = "Port should not be null")
    private String port;

    private String emailType;

    @Transient
    private String testMail;


}
