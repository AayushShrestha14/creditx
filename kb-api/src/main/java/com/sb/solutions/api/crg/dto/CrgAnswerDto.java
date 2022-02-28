package com.sb.solutions.api.crg.dto;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Amulya Shrestha on 7/21/2021
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrgAnswerDto extends BaseDto<Long> {
    private String description;

    private double points;

    private Status status;
}
