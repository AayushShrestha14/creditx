package com.sb.solutions.admin.approvallimit;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.api.approvallimit.service.ApprovalLimitService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/admin/approval-limit")
public class ApprovalLimitController {

    private final ApprovalLimitService approvalLimitService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> addApprovalLimit(@RequestBody ApprovalLimit approvalLimit) {
        final ApprovalLimit saved = approvalLimitService.save(approvalLimit);

        if (saved != null) {
            return new RestResponseDto().successModel(saved);
        } else {
            return new RestResponseDto().failureModel("Error Occurred");
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PreAuthorize("hasAuthority('Approval Limit')")
    @PostMapping(value = "/list")
    public ResponseEntity<?> getPageableBranch(@RequestBody Object obj,
                                               @RequestParam("page") int page,
                                               @RequestParam("size") int size) {
        return new RestResponseDto().successModel(approvalLimitService.findAllPageable(obj,
                PaginationUtils.pageable(page, size)));
    }


    @GetMapping(value = "/{id}/{loanCategory}/role")
    public ResponseEntity<?> getByRoleAndLoan(@PathVariable Long id, @PathVariable LoanApprovalType loanCategory) {
        return new RestResponseDto().successModel(approvalLimitService
            .getByRoleAndLoan(userService.getAuthenticatedUser().getRole().getId(), id,loanCategory
                    ));
    }

}
