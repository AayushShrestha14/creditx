package com.sb.solutions.web.cbsGroup.v1;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.cbsGroup.service.CbsGroupService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

/**
 * @author : Rujan Maharjan on  12/22/2020
 **/
@RestController
@RequestMapping(CbsGroupController.URL)
public class CbsGroupController {

    static final String URL = "v1/cbs";

    private final CbsGroupService cbsGroupService;

    public CbsGroupController(CbsGroupService cbsGroupService) {
        this.cbsGroupService = cbsGroupService;
    }

    @GetMapping
    public ResponseEntity<?> getCbs() {
        return new RestResponseDto().successModel(cbsGroupService.saveAll(new ArrayList<>()));
    }

    @GetMapping("/{obl}")
    public ResponseEntity<?> getCbsByCbsId(@PathVariable String obl) {
        return new RestResponseDto().successModel(cbsGroupService.findCbsGroupByObl(obl));
    }

    @PostMapping("/list")
    public ResponseEntity<?> getAllCbs(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(cbsGroupService.findAllPageable(searchDto, PaginationUtils
                .pageable(page, size)));
    }

}
