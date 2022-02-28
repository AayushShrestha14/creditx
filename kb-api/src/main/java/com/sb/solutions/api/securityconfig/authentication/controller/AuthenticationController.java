package com.sb.solutions.api.securityconfig.authentication.controller;


import com.google.common.base.Preconditions;
import com.sb.solutions.api.securityconfig.accesstoken.model.AccessToken;
import com.sb.solutions.api.securityconfig.authentication.dto.JwtResponse;
import com.sb.solutions.api.securityconfig.JwtUtil;
import com.sb.solutions.api.securityconfig.accesstoken.repository.AccessTokenRepository;
import com.sb.solutions.api.securityconfig.accesstoken.service.AccessTokenService;
import com.sb.solutions.api.securityconfig.authentication.dto.LoginRequest;
import com.sb.solutions.api.securityconfig.authentication.dto.TokenRefreshRequest;
import com.sb.solutions.api.securityconfig.authentication.dto.TokenRefreshResponse;
import com.sb.solutions.api.securityconfig.refreshtoken.model.RefreshToken;
import com.sb.solutions.api.securityconfig.refreshtoken.service.RefreshTokenService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.repository.UserRepository;
import com.sb.solutions.core.validation.constraint.SbValid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * @author : Rujan Maharjan on  6/12/2021
 **/

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class AuthenticationController {

    private static final String LOGIN_VALID_PARAMS = "username,password";
    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;


    private final JwtUtil jwtUtil;

    private final AccessTokenRepository accessTokenRepository;

    private final AccessTokenService accessTokenService;

    private final UserRepository userRepository;


    @PostMapping(path="/token",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @SbValid(LOGIN_VALID_PARAMS)
    public ResponseEntity<JwtResponse> authenticateUser( LoginRequest loginRequest) {
        Preconditions.checkNotNull(loginRequest);
        User user;
        try {
            log.info("attempting LOGIN:::  {}", loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            user = (User) userDetails;
            if (ObjectUtils.isEmpty(user)) {
                throw new BadCredentialsException("INVALID CREDENTIALS");
            }

        } catch (DisabledException | BadCredentialsException e) {
            throw new BadCredentialsException("INVALID CREDENTIALS");
        }

        String deviceId = loginRequest.getDeviceId();
        if(Objects.isNull(deviceId)){
            deviceId = "def";
        }

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId() ,deviceId);
        String token = accessTokenService.createToken(user, deviceId );
        JwtResponse response = new JwtResponse(token, refreshToken.getToken(), user.getUsername(), user.getEmail(), roles);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        String accessToken = request.getAccessToken();
        if (!jwtUtil.validateToken(accessToken)) {
            throw new BadCredentialsException("invalid Access token");
        }
        String deviceId = (String) jwtUtil.getDataFromToken(accessToken, "deviceId");
        Map<String, Object> claimsFromToken = jwtUtil.getClaimsFromToken(accessToken);
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    Long userId = refreshToken.getUserId();
                    User user = userRepository.getById(userId);
                    String token = jwtUtil.generateToken(user, deviceId);
                    AccessToken accessTokenByUserIdAndDeviceId = accessTokenRepository.findAccessTokenByUserIdAndDeviceId(userId, refreshToken.getDeviceId());
                    if (Objects.nonNull(accessTokenByUserIdAndDeviceId)) {
                        accessTokenByUserIdAndDeviceId.setAccessToken(token);
                        accessTokenRepository.save(accessTokenByUserIdAndDeviceId);
                    } else {
                        throw new IllegalStateException();
                    }
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new BadCredentialsException(
                        "Invalid Token"));
    }

}
