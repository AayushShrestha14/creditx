package com.sb.solutions.api.securityconfig.authentication.dto;

import lombok.Data;

/**
 * @author : Rujan Maharjan on  6/12/2021
 **/

@Data
public class TokenRefreshResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    public TokenRefreshResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
