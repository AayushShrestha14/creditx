package com.sb.solutions.web.customerInfo.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.DocAction;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerTransferDTO extends BaseDto<Long> {

    private Long customerInfoId;

    private Long fromUserId;

    private Long fromRoleId;

    private Long toUserId;

    private Long toRoleId;

    private Long toBranchId;

    private DocAction docAction;

    private String comment;

    private boolean notify = false;

}
