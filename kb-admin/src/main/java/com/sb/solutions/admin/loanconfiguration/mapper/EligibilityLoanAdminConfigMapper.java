package com.sb.solutions.admin.loanconfiguration.mapper;

import com.sb.solutions.admin.loanconfiguration.LoanConfigAdminDto;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.dto.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class EligibilityLoanAdminConfigMapper extends BaseMapper<LoanConfig, LoanConfigAdminDto> {

}
