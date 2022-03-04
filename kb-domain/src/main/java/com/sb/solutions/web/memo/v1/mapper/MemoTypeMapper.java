package com.sb.solutions.web.memo.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.memo.v1.dto.MemoTypeDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class MemoTypeMapper extends BaseMapper<MemoType, MemoTypeDto> {

}
