package com.sb.solutions.api.securityconfig.accesstoken.repository;

import com.sb.solutions.api.securityconfig.accesstoken.model.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author abishek on 2021-12-27
 */
@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken,Long> {
    AccessToken findAccessTokenByUserIdAndDeviceId(Long userId, String deviceId);
    void deleteAccessTokenByUserId(Long userId);
}
