package com.sb.solutions.web.memo.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.memo.v1.dto.MemoStageDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class MemoStageMapper extends BaseMapper<MemoStage, MemoStageDto> {

}
