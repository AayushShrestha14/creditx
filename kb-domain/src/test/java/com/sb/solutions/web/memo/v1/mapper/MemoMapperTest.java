package com.sb.solutions.web.memo.v1.mapper;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.sb.solutions.api.memo.entity.Memo;
import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.web.memo.v1.dto.MemoDto;
import com.sb.solutions.web.memo.v1.dto.MemoTypeDto;
import com.sb.solutions.web.memo.v1.dto.MemoUserDto;
import com.sb.solutions.web.memo.v1.dto.StageDto;

public class MemoMapperTest {

    private final MemoMapper mapper;

    public MemoMapperTest() {
        mapper = Mappers.getMapper(MemoMapper.class);
    }

    @Test
    public void testMapEntityToDtoReturnsDto() {
        final MemoType type = new MemoType();
        type.setName("Type");

        final User sentBy = new User();
        sentBy.setName("Sender");

        final User sentTo = new User();
        sentTo.setName("Receiver");

        final MemoStage stage = new MemoStage();
        stage.setNote("test note");
        stage.setSentBy(sentBy);
        stage.setSentTo(sentTo);

        final Memo memo = new Memo();
        memo.setSubject("test");
        memo.setContent("test content");
        memo.setSentBy(sentBy);
        memo.setSentTo(sentTo);
        memo.setType(type);
        memo.addMemoStage(stage);

        final MemoDto dto = mapper.mapEntityToDto(memo);

        assertThat(dto, notNullValue());
        assertThat(dto.getSubject(), equalTo(memo.getSubject()));
        assertThat(dto.getSentBy().getName(), equalTo(memo.getSentBy().getName()));
        assertThat(dto.getSentTo().getName(), equalTo(memo.getSentTo().getName()));
        assertThat(dto.getType().getName(), equalTo(memo.getType().getName()));
        assertThat(dto.getStages(), hasSize(1));

    }

    @Test
    public void testMapDtoToEntityReturnsEntity() {
        final MemoTypeDto type = new MemoTypeDto();
        type.setName("Type");

        final MemoUserDto sentBy = new MemoUserDto();
        sentBy.setName("Sender");

        final MemoUserDto sentTo = new MemoUserDto();
        sentTo.setName("Receiver");

        final StageDto stage = new StageDto();
        stage.setNote("test note");
        stage.setSentBy(sentBy);
        stage.setSentTo(sentTo);

        final MemoDto memo = new MemoDto();
        memo.setSubject("test");
        memo.setContent("test content");
        memo.setSentBy(sentBy);
        memo.setSentTo(sentTo);
        memo.setType(type);
        memo.addStage(stage);
        memo.setId(1L);

        final Memo entity = mapper.mapDtoToEntity(memo);
        entity.setId(memo.getId());

        assertThat(entity, notNullValue());
        assertThat(entity.getSubject(), equalTo(memo.getSubject()));
        assertThat(entity.getSentBy().getName(), equalTo(memo.getSentBy().getName()));
        assertThat(entity.getSentTo().getName(), equalTo(memo.getSentTo().getName()));
        assertThat(entity.getType().getName(), equalTo(memo.getType().getName()));

    }
}