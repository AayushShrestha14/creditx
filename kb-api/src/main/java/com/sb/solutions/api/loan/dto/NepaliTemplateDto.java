package com.sb.solutions.api.loan.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.NepaliTemplateType;

/**
 * @author Elvin Shrestha on 1/23/2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NepaliTemplateDto extends BaseDto<Long> {

    @Enumerated(value = EnumType.STRING)
    private NepaliTemplateType type;

    private String data;

}
