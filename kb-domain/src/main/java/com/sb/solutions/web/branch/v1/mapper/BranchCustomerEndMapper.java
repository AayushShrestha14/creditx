package com.sb.solutions.web.branch.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.branch.v1.dto.BranchCustomerEndDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class BranchCustomerEndMapper extends BaseMapper<Branch, BranchCustomerEndDto> {

}
