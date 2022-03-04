package com.sb.solutions.web.customerActivity.v1;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.service.CustomerActivityService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.EnumUtils;
import com.sb.solutions.core.utils.PaginationUtils;

/**
 * @author : Rujan Maharjan on  9/19/2020
 **/

@RestController
@RequestMapping(CustomerActivityController.URL)
public class CustomerActivityController {

    public static final String URL = "/v1/customer-activity";
    private final Logger logger = LoggerFactory.getLogger(CustomerActivityController.class);
    private final CustomerActivityService customerActivityService;

    public CustomerActivityController(
        @Autowired CustomerActivityService customerActivityService) {
        this.customerActivityService = customerActivityService;
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Map<String, String> searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        logger.info("search customer activity by {}", searchDto);
        return new RestResponseDto()
            .successModel(customerActivityService.findPageableBySpec(searchDto, PaginationUtils
                .pageableWithSort(page, size, "modifiedOn")));
    }


    @GetMapping
    public ResponseEntity<?> getActivity() {
        return new RestResponseDto()
            .successModel(EnumUtils.getNames(Activity.class));
    }


}
