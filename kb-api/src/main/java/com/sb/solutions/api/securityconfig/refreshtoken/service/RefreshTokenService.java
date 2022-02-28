package com.sb.solutions.api.securityconfig.refreshtoken.service;

import com.sb.solutions.api.securityconfig.refreshtoken.model.RefreshToken;
import com.sb.solutions.api.securityconfig.refreshtoken.repository.RefreshTokenRepository;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author : Rujan Maharjan on  6/12/2021
 **/
@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationDateInMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId , String deviceId) {
//        RefreshToken refreshToken = new RefreshToken();
//        Optional<RefreshToken> refreshTokenOPT = refreshTokenRepository.findRefreshTokenByUserIdAndDeviceId(userId, deviceId);
//        refreshToken.setDeviceId(deviceId);
//        if (refreshTokenOPT.isEmpty()) {
//            return createRefreshToken(userId, refreshToken);
//        } else {
//            try {
//                return verifyExpiration(refreshTokenOPT.get());
//            } catch (BadCredentialsException e) {
//                return createRefreshToken(userId, refreshToken);
//            }
//        }
        return null;
    }

    private RefreshToken createRefreshToken(Long userId, RefreshToken refreshToken) {
        User userById = userRepository.getById(userId);
        if (Objects.isNull(userId)){
            throw  new BadCredentialsException("UserId doesn't exist");
        }
        refreshToken.setUserId(userId);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new BadCredentialsException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteRefreshTokenByUserId(userId);
    }

}
