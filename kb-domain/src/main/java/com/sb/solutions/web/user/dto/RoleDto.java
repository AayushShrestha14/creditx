package com.sb.solutions.web.user.dto;

import lombok.Data;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.RoleType;

/**
 * @author Rujan Maharjan on 6/12/2019
 */

@Data
public class RoleDto extends BaseDto<Long> {

    private String roleName;

    private String authorityLabel;
    private RoleType roleType;
}
