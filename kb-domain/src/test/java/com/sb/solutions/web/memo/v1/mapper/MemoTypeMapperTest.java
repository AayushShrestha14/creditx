package com.sb.solutions.web.memo.v1.mapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.web.memo.v1.dto.MemoTypeDto;

public class MemoTypeMapperTest {

    private final MemoTypeMapper mapper;

    public MemoTypeMapperTest() {
        mapper = Mappers.getMapper(MemoTypeMapper.class);
    }

    @Test
    public void testMapEntityToDtoReturnsDto() {
        final MemoType entity = new MemoType();
        entity.setName("Memo Type");
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedAt(new Date());

        final MemoTypeDto dto = mapper.mapEntityToDto(entity);

        assertThat(dto, notNullValue());
        assertThat(dto.getName(), equalTo(entity.getName()));
        assertThat(dto.getStatus(), equalTo(entity.getStatus()));
        assertThat(dto.getCreatedAt(), equalTo(entity.getCreatedAt()));
    }

    @Test
    public void testMapDtoToEntityReturnsEntity() {
        final MemoTypeDto dto = new MemoTypeDto();
        dto.setName("Memo Type");
        dto.setStatus(Status.ACTIVE);
        dto.setCreatedAt(new Date());

        final MemoType entity = mapper.mapDtoToEntity(dto);

        assertThat(entity, notNullValue());
        assertThat(entity.getName(), equalTo(entity.getName()));
        assertThat(entity.getStatus(), equalTo(entity.getStatus()));
        assertThat(entity.getCreatedAt(), equalTo(entity.getCreatedAt()));
    }

    @Test
    public void testMapDtosToEntitesReturnsEntities() {
        final MemoTypeDto dto = new MemoTypeDto();
        dto.setName("Memo Type");
        dto.setStatus(Status.ACTIVE);
        dto.setCreatedAt(new Date());

        final MemoTypeDto dto1 = new MemoTypeDto();
        dto1.setName("Memo Type");
        dto1.setStatus(Status.INACTIVE);
        dto1.setCreatedAt(new Date());

        final List<MemoType> entites = mapper.mapDtosToEntities(Arrays.asList(dto, dto1));

        assertThat(entites, hasSize(2));
    }

    @Test
    public void testMapEntitiesToDtosReturnsDtos() {
        final MemoType entity = new MemoType();
        entity.setName("Memo Type");
        entity.setStatus(Status.ACTIVE);
        entity.setCreatedAt(new Date());

        final MemoType entity1 = new MemoType();
        entity1.setName("Memo Type");
        entity1.setStatus(Status.INACTIVE);
        entity1.setCreatedAt(new Date());

        final List<MemoTypeDto> dtos = mapper.mapEntitiesToDtos(Arrays.asList(entity, entity1));

        assertThat(dtos, hasSize(2));
    }
}