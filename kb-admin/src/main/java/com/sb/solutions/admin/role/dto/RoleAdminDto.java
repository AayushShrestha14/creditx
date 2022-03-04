package com.sb.solutions.admin.role.dto;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.RoleType;
import lombok.Data;

/**
 * @author Rujan Maharjan on 6/12/2019
 */

@Data
public class RoleAdminDto extends BaseDto<Long> {

    private String roleName;

    private String authorityLabel;
    private RoleType roleType;
}
