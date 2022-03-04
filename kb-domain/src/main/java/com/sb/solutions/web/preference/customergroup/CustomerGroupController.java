package com.sb.solutions.web.preference.customergroup;

import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.customerGroup.service.CustomerGroupService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@RequestMapping(CustomerGroupController.URL)
public class CustomerGroupController {

    static final String URL = "/v1/customer-group";

    private final CustomerGroupService customerGroupService;

    private final Logger logger = LoggerFactory.getLogger(CustomerGroupController.class);

    public CustomerGroupController(
        CustomerGroupService customerGroupService) {
        this.customerGroupService = customerGroupService;
    }


    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CustomerGroup customerGroup) {
        logger.debug("Request to save customer group");
        return new RestResponseDto().successModel(customerGroupService.save(customerGroup));
    }


    @GetMapping(path = "/{id}")
    public final ResponseEntity<?> findOne(@PathVariable long id) {
        logger.debug("Request to get customer group");
        Optional<CustomerGroup> customerGroup = customerGroupService.findOne(id);
        if (customerGroup.isPresent()) {
            return new RestResponseDto().successModel(customerGroup);
        }
        return new RestResponseDto().failureModel("No user found under provided id");
    }

    @PostMapping(path = "/list")
    public final ResponseEntity<?> getPageableCustomerGroup(
        @RequestBody(required = false) Map<String, String> requestParams,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        logger.debug("Request to get customer group pageable");
        final Page<CustomerGroup> customerGroups = customerGroupService
            .findPageableBySpec(requestParams,
                PaginationUtils.pageable(page, size));
        return new RestResponseDto().successModel(customerGroups);
    }

    @GetMapping(path = "/all")
    public final ResponseEntity<?> getAllCustomerGroup() {
        logger.debug("Request to get all customer group");
        return new RestResponseDto().successModel(customerGroupService.findAll());
    }

}
