package com.sb.solutions.web.loanflag;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.loanflag.service.CustomerLoanFlagService;
import com.sb.solutions.core.dto.RestResponseDto;

/**
 * @author Elvin Shrestha on 4/29/2020
 */
@RestController
@RequestMapping(CustomerLoanFlagController.URL)
public class CustomerLoanFlagController {

    static final String URL = "/v1/customer-loan-flag";

    private final CustomerLoanFlagService service;

    public CustomerLoanFlagController(
        CustomerLoanFlagService service) {
        this.service = service;
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllWithSearch(@RequestBody Object search) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.convertValue(search, Map.class);
        return new RestResponseDto().successModel(service.findAllBySpec(map));
    }
}
