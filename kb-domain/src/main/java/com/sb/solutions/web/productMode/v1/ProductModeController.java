package com.sb.solutions.web.productMode.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.productMode.service.ProductModeService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.BankUtils;
import com.sb.solutions.core.utils.ProductUtils;

/**
 * @author : Rujan Maharjan on  12/15/2019
 **/
@RestController
@RequestMapping(ProductModeController.URL)
public class ProductModeController {

    static final String URL = "/v1/product-mode";

    private static final Logger logger = LoggerFactory.getLogger(ProductModeController.class);

    private ProductModeService productModeService;

    public ProductModeController(
        ProductModeService productModeService) {
        this.productModeService = productModeService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getProductMode() {
        logger.info("get product mode {}", URL);
        return new RestResponseDto().successModel(productModeService.findAll());
    }

    @GetMapping()
    public ResponseEntity<?> getProductUtils() {
        logger.info("get product mode utils{}", URL);
        return new RestResponseDto().successModel(ProductUtils.getProductUtilsMap());
    }

    @GetMapping(value = "/bankUtils")
    public ResponseEntity<?> getBankUtils() {
        return new RestResponseDto().successModel(BankUtils.getMap());
    }

}
