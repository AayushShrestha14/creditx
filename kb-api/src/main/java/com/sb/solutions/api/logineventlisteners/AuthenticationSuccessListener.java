package com.sb.solutions.api.logineventlisteners;

import com.sb.solutions.api.logindetail.service.LoginDetailService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private LoginDetailService loginDetailService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        String userName = event.getAuthentication().getName();
        SecurityContextHolder.getContext().setAuthentication(event.getAuthentication());
        loginDetailService.resetDetailForUser(userName);
    }
}
