package com.sb.solutions.admin.role.mapper;

import com.sb.solutions.admin.role.dto.RoleHierarchyAdminDto;
import com.sb.solutions.api.authorization.entity.RoleHierarchy;
import com.sb.solutions.core.dto.BaseMapper;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author Rujan Maharjan on 5/13/2019
 */

@Component
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class RoleHierarchyAdminMapper extends BaseMapper<RoleHierarchy, RoleHierarchyAdminDto> {

}
