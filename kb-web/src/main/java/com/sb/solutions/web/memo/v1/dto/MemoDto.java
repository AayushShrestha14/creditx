package com.sb.solutions.web.memo.v1.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.memo.enums.Stage;
import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemoDto extends BaseDto<Long> {

    @NotNull
    private String subject;

    @NotNull
    private String content;

    private String refNumber;

    private MemoUserDto sentBy;

    private MemoUserDto sentTo;

    private Set<MemoUserDto> cc;

    private Set<MemoUserDto> bcc;

    private Status status = Status.ACTIVE;

    private Stage stage = Stage.DRAFT;

    private MemoTypeDto type;

    private Set<StageDto> stages = new HashSet<>();

    public void addStage(StageDto s) {
        stages.add(s);
    }
}
