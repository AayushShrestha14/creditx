package com.sb.solutions.web.common.stage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.web.user.dto.RoleDto;
import com.sb.solutions.web.user.dto.UserDto;

/**
 * @author Sunil Babu Shrestha on 5/26/2019
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StageDto extends BaseDto<Long> {

    private Long customerLoanId;

    private Long loanConfigId;

    private DocAction docAction;

    private UserDto fromUser;

    private RoleDto fromRole;

    private UserDto toUser;

    private RoleDto toRole;

    private DocStatus documentStatus;

    private String comment;
    /*
    This notify flag will set during Approval Stage only
     */
    private boolean notify = false;

    private Long customerOfferLetterId;

    private Boolean isSol = Boolean.FALSE;

    private UserDto solUser;


}
