package com.sb.solutions.web.mGroup.v1.mapper;

import com.sb.solutions.api.mGroupInfo.entity.MGroupInfo;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.mGroup.v1.dto.MGroupDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class MGroupMapper extends BaseMapper<MGroupInfo, MGroupDto> {
}
