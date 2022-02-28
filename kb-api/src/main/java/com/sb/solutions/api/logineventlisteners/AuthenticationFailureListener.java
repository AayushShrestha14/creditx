package com.sb.solutions.api.logineventlisteners;

import com.sb.solutions.api.logindetail.service.LoginDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private LoginDetailService loginDetailService;

    private final Integer ALLOWED_ATTEMPTS = 6;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        String username =  event.getAuthentication().getName();
        loginDetailService.incrementLoginAttempt(username,1,ALLOWED_ATTEMPTS);
    }

}
