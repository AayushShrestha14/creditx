package com.sb.solutions.core.utils.email.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Elvin Shrestha on 9/13/2020
 */
@Data
@AllArgsConstructor
public class InsuranceEmailDto {

    private String companyName;
    private Date expiryDate;
}
