package com.sb.solutions.core.config.security;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class AccountLockedException extends InternalAuthenticationServiceException {
    public AccountLockedException() {
        super("Account is Locked");
    }

}
