package com.sb.solutions.api.loan.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.loan.dto.NepaliTemplateDto;
import com.sb.solutions.api.nepalitemplate.entity.NepaliTemplate;
import com.sb.solutions.core.dto.BaseMapper;

/**
 * @author Elvin Shrestha on 1/23/2020
 */
@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class NepaliTemplateMapper extends BaseMapper<NepaliTemplate, NepaliTemplateDto> {

}
