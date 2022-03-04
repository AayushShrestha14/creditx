package com.sb.solutions.web.document.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.DocumentCheckType;
import com.sb.solutions.core.enums.Status;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DocumentDto extends BaseDto<Long> {

    private String displayName;

    private String url;

    private Status status;

    private DocumentCheckType checkType;

    private Boolean containsTemplate;


}
