package com.sb.solutions.dto;

import com.sb.solutions.entity.CadAdditionalDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.dto.BaseDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Rujan Maharjan on  1/5/2021
 **/

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExposureDto extends BaseDto<Long> {

    private Long cadId;

    private String historyData;

    private List<UploadDto> exposureDocument = new ArrayList<>();

    private String exposureComment;

}
