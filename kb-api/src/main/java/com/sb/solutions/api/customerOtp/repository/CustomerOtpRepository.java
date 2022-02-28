package com.sb.solutions.api.customerOtp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.customerOtp.entity.CustomerOtp;

@Repository
public interface CustomerOtpRepository extends JpaRepository<CustomerOtp, Long> {

    CustomerOtp findCustomerOtpByEmailOrMobile(String email, String mobile);

}
