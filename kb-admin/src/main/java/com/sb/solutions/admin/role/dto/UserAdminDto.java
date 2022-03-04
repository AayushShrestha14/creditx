package com.sb.solutions.admin.role.dto;

import com.sb.solutions.api.branch.dto.BranchDto;
import com.sb.solutions.core.dto.BaseDto;
import lombok.Data;

import java.util.List;

/**
 * @author Rujan Maharjan on 6/4/2019
 */

@Data

public class UserAdminDto extends BaseDto<Long> {

    private List<BranchDto> branch;
    private RoleAdminDto role;

}
