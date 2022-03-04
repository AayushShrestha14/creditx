package com.sb.solutions.web.memo.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.memo.v1.dto.MemoDto;
import com.sb.solutions.web.memo.v1.dto.MemoTypeDto;
import com.sb.solutions.web.memo.v1.dto.MemoUserDto;
import com.sb.solutions.web.memo.v1.dto.StageDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class MemoMapper extends BaseMapper<Memo, MemoDto> {

    public abstract MemoTypeDto mapMemoTypeToMemoTypeDto(MemoType entity);

    public abstract MemoType mapMemoTypeDtoToMemoType(MemoTypeDto dto);

    public abstract StageDto mapMemoStageDtoToMemoStage(StageDto dto);

    public abstract MemoStage mapMemoStageToMemoStageDto(MemoStage entity);

    public abstract MemoUserDto mapUserToMemoUserDto(User entity);

    public abstract MemoUserDto mapMemoUserDtoToUser(MemoUserDto dto);
}
