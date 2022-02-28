package com.sb.solutions.api.crg.mapper;

import com.sb.solutions.api.crg.dto.CrgQuestionDto;
import com.sb.solutions.api.crg.entity.CrgQuestion;
import com.sb.solutions.core.dto.BaseMapper;
import org.mapstruct.Mapper;

/**
 * @author Amulya Shrestha on 7/21/2021
 **/

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class CrgQuestionMapper extends BaseMapper<CrgQuestion, CrgQuestionDto> {

}
