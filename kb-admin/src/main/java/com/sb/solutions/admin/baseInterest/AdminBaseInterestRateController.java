package com.sb.solutions.admin.baseInterest;

import com.sb.solutions.api.preference.interestrate.entity.BaseInterestRate;
import com.sb.solutions.api.preference.interestrate.service.BaseInterestService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author abishek on 2022-03-03
 */

@RestController
@RequestMapping("/v1/admin/base-interest")
public class AdminBaseInterestRateController {

    private final BaseInterestService baseInterestService;

    @Autowired
    public AdminBaseInterestRateController(BaseInterestService baseInterestService) {
        this.baseInterestService = baseInterestService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAll(@RequestBody SearchDto searchDto,
                                    @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
                .successModel(baseInterestService.findAllPageable(searchDto, PaginationUtils
                        .pageable(page, size)));
    }


    @GetMapping("/all")
    public List<BaseInterestRate> getAllInterests() {
        return baseInterestService.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseInterestRate> getInterestById(@PathVariable("id") Long id) {
        BaseInterestRate base = baseInterestService.findOne(id);
        if (base == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(base);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateIntrest(@PathVariable("id") Long id,
                                           @Valid @RequestBody BaseInterestRate intrestDetail) {
        BaseInterestRate base = baseInterestService.findOne(id);
        if (base == null) {
            return new RestResponseDto().failureModel("Unable to save value");
        }

        return new RestResponseDto().successModel(baseInterestService.save(intrestDetail));
    }


    @PostMapping
    public ResponseEntity<?> saveInterest(@Valid @RequestBody BaseInterestRate base) {
        return new RestResponseDto().successModel(baseInterestService.save(base));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveBaseRate() {
        return new RestResponseDto().successModel(baseInterestService.getActiveRate());
    }

}
