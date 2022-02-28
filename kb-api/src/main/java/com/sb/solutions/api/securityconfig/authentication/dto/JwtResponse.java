package com.sb.solutions.api.securityconfig.authentication.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : Rujan Maharjan on  6/12/2021
 **/

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;


    public JwtResponse(String accessToken, String refreshToken, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.email = email;
        this.roles = roles;

    }
}
