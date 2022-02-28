package com.sb.solutions.web.customer.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.customerOtp.entity.CustomerOtp;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.customer.v1.dto.CustomerOtpTokenDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class CustomerOtpTokenMapper extends BaseMapper<CustomerOtp, CustomerOtpTokenDto> {

}
