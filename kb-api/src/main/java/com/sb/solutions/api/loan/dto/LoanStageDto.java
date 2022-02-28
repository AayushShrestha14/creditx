package com.sb.solutions.api.loan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.authorization.dto.RoleDto;
import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.DocAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoanStageDto extends BaseDto<Long> {

    private UserDto fromUser;

    private RoleDto fromRole;

    private UserDto toUser;

    private RoleDto toRole;

    private DocAction docAction;

    private String comment;

}
