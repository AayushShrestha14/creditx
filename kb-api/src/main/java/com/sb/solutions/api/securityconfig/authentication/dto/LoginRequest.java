package com.sb.solutions.api.securityconfig.authentication.dto;

import lombok.Data;

/**
 * @author : Rujan Maharjan on  6/12/2021
 **/
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String deviceId;

}
