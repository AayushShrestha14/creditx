package com.sb.solutions.web.memo.v1.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.validation.constraint.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoTypeDto extends BaseDto<Long> {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Status status = Status.ACTIVE;
}
