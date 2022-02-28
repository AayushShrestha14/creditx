package com.sb.solutions.web.mGroup.v1.dto;

import com.sb.solutions.core.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MGroupDto extends BaseDto<Long> {
    private Long customerInfoId;
    private String groupCode;
    private String detailInformation;
}
