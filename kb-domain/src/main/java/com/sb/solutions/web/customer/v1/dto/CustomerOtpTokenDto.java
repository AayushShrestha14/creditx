package com.sb.solutions.web.customer.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOtpTokenDto {

    private Long id;

    private String otp;

}
