package com.sb.solutions.api.siteVisit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankExposureAssets {

    private String bankName;

    private double amount;

}
