package com.sb.solutions.api.customerOtp.service;

import com.sb.solutions.api.customerOtp.entity.CustomerOtp;
import com.sb.solutions.core.service.BaseService;

public interface CustomerOtpService extends BaseService<CustomerOtp> {

    void delete(CustomerOtp customerOtp);

    CustomerOtp findByEmailOrMobile(String email, String mobile);

}
