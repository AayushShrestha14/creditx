package com.sb.solutions.admin.accountoppening.accountCategory;

import com.sb.solutions.api.accountCategory.entity.AccountCategory;
import com.sb.solutions.api.accountCategory.service.AccountCategoryService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(AccountAdminCategoryController.URL)
public class AccountAdminCategoryController {

    static final String URL = "/v1/admin/accountCategory";

    private final AccountCategoryService accountCategoryService;

    public AccountAdminCategoryController(
        @Autowired AccountCategoryService accountCategoryService
    ) {
        this.accountCategoryService = accountCategoryService;
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody AccountCategory accountCategory) {
        AccountCategory a = accountCategoryService.save(accountCategory);
        return new RestResponseDto().successModel(a);
    }

    @PostMapping(value = "/list")
    public ResponseEntity<?> getPageable(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            accountCategoryService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(accountCategoryService.findAll());
    }

    @GetMapping(value = "/accountType/{accountTypeId}")
    public ResponseEntity<?> getAccountCategoryByAccountType(@PathVariable Long accountTypeId) {
        return new RestResponseDto()
            .successModel(accountCategoryService.findAllByAccountTypeId(accountTypeId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id, @Valid @RequestBody AccountCategory accountCategory) {

        final AccountCategory savedAccountCategory = accountCategoryService.save(accountCategory);

        if (null == savedAccountCategory) {
            return new RestResponseDto()
                .failureModel("Error occurred while saving Account Category " + accountCategory);
        }

        return new RestResponseDto().successModel(savedAccountCategory);
    }

}
