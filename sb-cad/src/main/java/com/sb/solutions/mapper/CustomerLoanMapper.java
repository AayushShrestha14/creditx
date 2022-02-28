package com.sb.solutions.mapper;


import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.dto.CustomerLoanDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author : Rujan Maharjan on  12/1/2020
 **/
@Component
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class CustomerLoanMapper extends BaseMapper<CustomerLoan, CustomerLoanDto> {
}
