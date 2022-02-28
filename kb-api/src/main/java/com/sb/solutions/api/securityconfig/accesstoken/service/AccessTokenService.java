package com.sb.solutions.api.securityconfig.accesstoken.service;

import com.sb.solutions.api.securityconfig.JwtUtil;
import com.sb.solutions.api.securityconfig.accesstoken.model.AccessToken;
import com.sb.solutions.api.securityconfig.accesstoken.repository.AccessTokenRepository;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

/**
 * @author abishek on 2021-12-27
 */
@Service
@RequiredArgsConstructor
public class AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    public void invalidateAccessToken(Long userId){
        accessTokenRepository.deleteAccessTokenByUserId(userId);
    }

    @Transactional
    public String createToken(User user, String deviceId){
        AccessToken accessTokenByUserIdAndDeviceId = accessTokenRepository.findAccessTokenByUserIdAndDeviceId(user.getId(), deviceId);
        if(Objects.isNull(accessTokenByUserIdAndDeviceId)){
            AccessToken accessToken = generateAccessToken(jwtUtil, user, deviceId);
            accessTokenRepository.save(accessToken);
            return accessToken.getAccessToken();
        }else{
             String accessToken = accessTokenByUserIdAndDeviceId.getAccessToken();
             try {
                  if(jwtUtil.validateToken(accessToken)){
                      return accessToken;
                  }
             }catch (Exception exception){
                 accessTokenRepository.delete(accessTokenByUserIdAndDeviceId);
                 AccessToken accessTokenNew = generateAccessToken(jwtUtil, user, deviceId);
                 accessTokenRepository.save(accessTokenNew);
                 return accessTokenNew.getAccessToken();
             }
        }
        return null;
    }

    private AccessToken generateAccessToken(JwtUtil jwtUtil, User user, String deviceId ) {
        String token = jwtUtil.generateToken(user, deviceId);
        AccessToken accessTokenNew = new AccessToken();
        accessTokenNew.setAccessToken(token);
        accessTokenNew.setUserId(user.getId());
        accessTokenNew.setDeviceId(deviceId);
        return accessTokenNew;
    }

    public  boolean existsToken(String accessToken ){
        if (Objects.isNull(accessToken)){
            return false;
        }
        String usernameFromToken = jwtUtil.getUsernameFromToken(accessToken);
        User userByUsername = userRepository.getUsersByUsername(usernameFromToken);
        String deviceId = (String)jwtUtil.getDataFromToken(accessToken, "deviceId");
        AccessToken tokenByUserName = accessTokenRepository.findAccessTokenByUserIdAndDeviceId(userByUsername.getId(), deviceId);
        if (Objects.isNull(tokenByUserName)){
            return false;
        }
        return  tokenByUserName.getAccessToken().equals(accessToken);
    }

}
