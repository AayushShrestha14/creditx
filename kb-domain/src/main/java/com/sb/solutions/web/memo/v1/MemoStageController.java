package com.sb.solutions.web.memo.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.memo.entity.MemoStage;
import com.sb.solutions.api.memo.service.MemoStageService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.web.memo.v1.mapper.MemoStageMapper;

@RestController
@RequestMapping(MemoStageController.URL)
public class MemoStageController {

    public static final String URL = "v1/memos/{id}/stages";

    private final MemoStageService service;
    private final MemoStageMapper mapper;


    public MemoStageController(
        @Autowired MemoStageService service,
        @Autowired MemoStageMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<?> getMemoStagesByMemo(@PathVariable long id) {
        return new RestResponseDto()
            .successModel(
                mapper
                    .mapEntityToDto(service.findOne(id))
            );
    }

    @PostMapping
    public ResponseEntity<?> saveMemoStageForMemo(@PathVariable long id,
        @RequestBody MemoStage stage) {

        final MemoStage saved = service.save(stage);

        if (null == saved) {
            return new RestResponseDto()
                .failureModel("Error occurred while saving Memo " + stage);
        }

        return new RestResponseDto().successModel(saved);
    }
}
