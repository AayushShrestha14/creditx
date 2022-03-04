package com.sb.solutions.web.memo.v1;

import java.util.Date;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.memo.entity.MemoType;
import com.sb.solutions.api.memo.service.MemoTypeService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.web.memo.v1.dto.MemoTypeDto;
import com.sb.solutions.web.memo.v1.mapper.MemoTypeMapper;

@RestController
@RequestMapping(MemoTypeController.URL)
public class MemoTypeController {

    static final String URL = "/v1/memos/types";

    private final MemoTypeService service;

    private final MemoTypeMapper mapper;

    public MemoTypeController(@Autowired MemoTypeService service,
        @Autowired MemoTypeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody MemoTypeDto type) {

        final MemoType memoType = mapper.mapDtoToEntity(type);

        final MemoType savedMemo = service.save(memoType);

        if (null == savedMemo) {
            return new RestResponseDto()
                .failureModel("Error occurred while saving Memo Type " + type);
        }

        return new RestResponseDto().successModel(savedMemo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable long id) {
        return new RestResponseDto().successModel(service.findOne(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody MemoTypeDto dto) {

        final MemoType type = mapper.mapDtoToEntity(dto);
        type.setLastModifiedAt(new Date());

        final MemoType savedMemo = service.save(type);

        if (null == savedMemo) {
            return new RestResponseDto()
                .failureModel("Error occurred while saving Memo Type " + type);
        }

        return new RestResponseDto().successModel(savedMemo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        service.deleteById(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(
        @RequestParam(required = false) Map<String, String> searchParams,
        @RequestParam(defaultValue = "1", name = "page") int page,
        @RequestParam(defaultValue = "20", name = "size") int size) {

        final Pageable pageable = PaginationUtils.pageable(page, size);

        final Page<MemoType> types = service
            .findPageable(PaginationUtils.excludePageableProperties(searchParams),
                PaginationUtils.pageable(page, size));

        final Page<MemoTypeDto> dtos = new PageImpl<>(mapper.mapEntitiesToDtos(types.getContent()),
            pageable, types.getTotalElements());

        return ResponseEntity.ok(dtos);
    }
}
