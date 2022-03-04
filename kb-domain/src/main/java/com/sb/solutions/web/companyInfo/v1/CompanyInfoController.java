package com.sb.solutions.web.companyInfo.v1;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.companyInfo.model.entity.CompanyInfo;
import com.sb.solutions.api.companyInfo.model.service.CompanyInfoService;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.aop.CustomerActivityLog;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;


@RestController
@RequestMapping(value = "/v1/companyInfo")
public class CompanyInfoController {

    private final CompanyInfoService companyInfoService;

    public CompanyInfoController(@Autowired CompanyInfoService companyInfoService) {
        this.companyInfoService = companyInfoService;
    }

    @CustomerActivityLog(Activity.CUSTOMER_UPDATE)
    @PostMapping
    public ResponseEntity<?> saveCompanyInfo(@Valid @RequestBody CompanyInfo companyInfo) {
        return new RestResponseDto().successModel(companyInfoService.save(companyInfo));
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(companyInfoService.findAllPageable(searchDto, PaginationUtils
                .pageable(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyInfoByID(
            @PathVariable("id") Long id) {
        return new RestResponseDto()
                .successModel(companyInfoService.findOne(id));
    }

    @GetMapping("/registrationNumber/{id}")
    public ResponseEntity<?> getCompanyInfoByRegNumber(
        @PathVariable("id") String id) {
        return new RestResponseDto()
            .successModel(companyInfoService.findCompanyInfoByRegistrationNumber(id));
    }

    @GetMapping("/panNumber/{id}")
    public ResponseEntity<?> getCompanyInfoByPanNumber(
        @PathVariable("id") String id) {
        return new RestResponseDto().successModel(companyInfoService.findCompanyInfoByPanNumber(id));
    }
}
