package com.sb.solutions.web.document.v1.mapper;

import org.mapstruct.Mapper;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.dto.BaseMapper;
import com.sb.solutions.web.document.v1.dto.DocumentDto;

@Mapper(componentModel = BaseMapper.SPRING_MODEL)
public abstract class DocumentMapper extends BaseMapper<Document, DocumentDto> {

}
