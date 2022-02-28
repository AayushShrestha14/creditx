package com.sb.solutions.web.customer.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOtpDto {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String mobile;

}
