package com.sb.solutions.api.crg.dto;

import com.sb.solutions.core.dto.BaseDto;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Amulya Shrestha on 7/21/2021
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrgQuestionDto extends BaseDto<Long> {
    private String description;

    private double maximumPoints;

    private long appearanceOrder;

    private Status status;

    private long crgGroupId;

    private List<CrgAnswerDto> answers = new ArrayList<>();
}
