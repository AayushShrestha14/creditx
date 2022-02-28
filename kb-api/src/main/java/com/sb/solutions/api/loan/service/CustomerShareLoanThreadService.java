package com.sb.solutions.api.loan.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sb.solutions.api.sharesecurity.service.ShareSecurityService;

/**
 * @author Sunil Babu Shrestha on 1/29/2020
 */
@Service
public class CustomerShareLoanThreadService implements Runnable {

    private final ShareSecurityService shareLoanHelper;

    public CustomerShareLoanThreadService(
        ShareSecurityService shareLoanHelper
    ) {
        this.shareLoanHelper = shareLoanHelper;
    }

    @Override
    public void run() {
        shareLoanHelper.execute(Optional.empty());
    }
}
