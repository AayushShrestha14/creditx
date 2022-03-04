package com.sb.solutions.web.sector.subSector;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.sector.subsector.entity.SubSector;
import com.sb.solutions.api.sector.subsector.service.SubSectorService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/sub-sector")
public class SubSectorController {

    private final SubSectorService subSectorService;

    @PostMapping
    public ResponseEntity<?> saveSubSector(@RequestBody SubSector subSector) {
        return new RestResponseDto().successModel(subSectorService.save(subSector));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAll(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            subSectorService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping("/statusCount")
    public ResponseEntity<?> getSubSectorStatusCount() {
        return new RestResponseDto().successModel(subSectorService.subSectorStatusCount());
    }
}
