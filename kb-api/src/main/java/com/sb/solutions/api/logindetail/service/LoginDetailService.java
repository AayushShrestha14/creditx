package com.sb.solutions.api.logindetail.service;

import com.sb.solutions.api.user.entity.User;

public interface LoginDetailService {
    void incrementLoginAttempt(String user, int i, int allowedAttempts);
    void resetDetailForUser(String user);
}
