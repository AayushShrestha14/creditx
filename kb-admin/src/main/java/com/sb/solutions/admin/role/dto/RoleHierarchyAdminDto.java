package com.sb.solutions.admin.role.dto;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.core.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Rujan Maharjan on 5/13/2019
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RoleHierarchyAdminDto extends BaseDto<Long> {

    private Role role;
    private Long roleOrder;
}
