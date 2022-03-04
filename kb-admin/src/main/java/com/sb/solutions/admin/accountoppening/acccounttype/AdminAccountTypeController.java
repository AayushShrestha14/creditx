package com.sb.solutions.admin.accountoppening.acccounttype;

import com.sb.solutions.api.accountType.entity.AccountType;
import com.sb.solutions.api.accountType.service.AccountTypeService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(AdminAccountTypeController.URL)
public class AdminAccountTypeController {

    static final String URL = "/v1/admin/accountType";

    private final AccountTypeService accountTypeService;

    public AdminAccountTypeController(
        @Autowired AccountTypeService accountTypeService
    ) {
        this.accountTypeService = accountTypeService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody AccountType accountType) {
        AccountType a = accountTypeService.save(accountType);
        if (a == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(a);
        }
    }

    @PostMapping(value = "/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            accountTypeService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new RestResponseDto().successModel(accountTypeService.findOne(id));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(accountTypeService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody AccountType accountType) {

        final AccountType savedAccountType = accountTypeService.save(accountType);

        if (null == savedAccountType) {
            return new RestResponseDto()
                .failureModel("Error occurred while saving Account Type " + accountType);
        }

        return new RestResponseDto().successModel(savedAccountType);
    }

}
