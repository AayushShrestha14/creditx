package com.sb.solutions.web.memo.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.memo.enums.Stage;
import com.sb.solutions.core.dto.BaseDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoStageDto extends BaseDto<Long> {

    private MemoDto memo;

    private MemoUserDto sentBy;

    private MemoUserDto sentTo;

    private Stage stage;

    private String note;
}
