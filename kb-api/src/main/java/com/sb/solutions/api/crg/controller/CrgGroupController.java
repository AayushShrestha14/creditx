package com.sb.solutions.api.crg.controller;

import com.sb.solutions.api.crg.dto.CrgGroupDto;
import com.sb.solutions.api.crg.entity.CrgGroup;
import com.sb.solutions.api.crg.service.CrgGroupService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */

@RestController
@RequestMapping("/v1/crg-group")
@RequiredArgsConstructor
public class CrgGroupController {
    private final CrgGroupService crgGroupService;

    @PostMapping
    public final ResponseEntity<?> addGroup(@Valid @RequestBody CrgGroup group) {

        final CrgGroup crgGroup = crgGroupService.save(group);
        return new RestResponseDto().successModel(crgGroup);
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestBody CrgGroupDto dto) {
        CrgGroup group = crgGroupService.findOne(dto.getId()).orElse(null);
        if (group == null) {
            return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "Not found!!!");
        }
        group.setStatus(dto.getStatus());
        return new RestResponseDto().successModel(crgGroupService.save(group));
    }

    @GetMapping("/all")
    public final ResponseEntity<?> findAllGroup() {
        return new RestResponseDto().successModel(crgGroupService.findAll());
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Map<String, String> searchParams,
            @RequestParam(defaultValue = "1", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "size") int size) {

        final Page<CrgGroup> groups = crgGroupService
                .findPageable(PaginationUtils.excludePageableProperties(searchParams),
                        PaginationUtils.pageable(page, size));

        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody CrgGroup type) {

        type.setLastModifiedAt(new Date());

        final CrgGroup savedCrgGroup = crgGroupService.save(type);

        if (null == savedCrgGroup) {
            return new RestResponseDto()
                    .failureModel("Error occurred while saving Crg Group! " + type);
        }

        return new RestResponseDto().successModel(savedCrgGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        crgGroupService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}
