package com.sb.solutions.api.customerOtp.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOtp {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private String mobile;

    private String otp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;
}
