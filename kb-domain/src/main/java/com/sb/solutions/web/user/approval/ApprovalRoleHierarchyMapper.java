package com.sb.solutions.web.user.approval;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchy;
import com.sb.solutions.core.dto.BaseMapper;

@Component
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class ApprovalRoleHierarchyMapper extends
    BaseMapper<ApprovalRoleHierarchy, ApprovalRoleHierarchyDto> {

}
