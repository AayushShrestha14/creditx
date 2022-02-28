package com.sb.solutions.web.eligibility.v1.loanconfig.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.eligibility.v1.loanconfig.dto.LoanConfigDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class EligibilityLoanConfigMapper extends BaseMapper<LoanConfig, LoanConfigDto> {

}
