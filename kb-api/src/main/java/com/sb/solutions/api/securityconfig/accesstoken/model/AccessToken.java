package com.sb.solutions.api.securityconfig.accesstoken.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author abishek on 2021-12-27
 */
@Entity
@Getter
@Setter
public class AccessToken {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    private String deviceId;

    @Lob
    private String accessToken;

}
