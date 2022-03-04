package com.sb.solutions.web.nepsePriceInfo.controller;

import com.sb.solutions.api.nepseCompany.service.NepsePriceInfoService;
import com.sb.solutions.core.dto.RestResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/nepse-price-info")
public class NepsePriceinfoController {
    private final NepsePriceInfoService nepsePriceinfoController;

    public NepsePriceinfoController(NepsePriceInfoService nepsePriceinfoController) {
        this.nepsePriceinfoController = nepsePriceinfoController;
    }

    @GetMapping(value = "/active-data")
    public ResponseEntity<?> getActivePriceInfo() {
        return new RestResponseDto().successModel(nepsePriceinfoController.getActivePriceInfo());
    }
}
