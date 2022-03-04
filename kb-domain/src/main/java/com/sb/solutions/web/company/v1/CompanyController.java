package com.sb.solutions.web.company.v1;

import javax.validation.Valid;

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

import com.sb.solutions.api.company.entity.Company;
import com.sb.solutions.api.company.service.CompanyService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> saveCompany(@Valid @RequestBody Company company) {
        return new RestResponseDto().successModel(companyService.save(company));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAllPage(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            companyService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping("/statusCount")
    public ResponseEntity<?> getCompanyStatusCount() {
        return new RestResponseDto().successModel(companyService.companyStatusCount());
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllCompany() {
        return new RestResponseDto().successModel(companyService.findAll());
    }
}
