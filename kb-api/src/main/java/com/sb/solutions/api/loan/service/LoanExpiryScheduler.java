package com.sb.solutions.api.loan.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.sb.solutions.api.insurance.service.InsuranceService;

/**
 * @author Aashish Shrestha, 12/Mar/2020
 */

@Configuration
public class LoanExpiryScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanExpiryScheduler.class);
    private final InsuranceService insuranceService;

    public LoanExpiryScheduler(
        InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @Scheduled(cron = "0 0 23 * * ?") // every day at 11pm
    public void runInsuranceScheduler() {
        LOGGER.info("Insurance Expiry Scheduler running !!");
        try {
            this.insuranceService.execute(Optional.empty());
        } catch (Exception e) {
            LOGGER.debug("Error running loan expiry scheduler: {}", e.getLocalizedMessage());
        }
    }
}

