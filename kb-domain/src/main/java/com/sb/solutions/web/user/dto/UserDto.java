package com.sb.solutions.web.user.dto;

import java.util.List;

import lombok.Data;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.web.branch.v1.dto.BranchDto;

/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Data

public class UserDto extends BaseDto<Long> {

    private List<BranchDto> branch;
    private RoleDto role;

}
