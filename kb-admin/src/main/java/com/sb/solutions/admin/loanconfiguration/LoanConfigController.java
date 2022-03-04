package com.sb.solutions.admin.loanconfiguration;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.loanConfig.service.LoanConfigService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.web.eligibility.v1.loanconfig.dto.LoanConfigDto;
import com.sb.solutions.web.eligibility.v1.loanconfig.mapper.EligibilityLoanConfigMapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rujan Maharjan on 2/25/2019
 */
@RestController
@RequestMapping("/v1/admin/loan-configs")
public class LoanConfigController {

    private final Logger logger = LoggerFactory.getLogger(LoanConfigController.class);

    private LoanConfigService loanConfigService;

    private EligibilityLoanConfigMapper eligibilityLoanConfigMapper;

    public LoanConfigController(
        @Autowired LoanConfigService loanConfigService,
        @Autowired EligibilityLoanConfigMapper eligibilityLoanConfigMapper
    ) {
        this.loanConfigService = loanConfigService;
        this.eligibilityLoanConfigMapper = eligibilityLoanConfigMapper;
    }

    @RequestMapping(method = RequestMethod.POST) public ResponseEntity<?> saveLoanConfiguration(@Valid @RequestBody LoanConfig config) {
        logger.debug("Request to save new Loan.");
        final LoanConfig loanConfig = loanConfigService.save(config);

        if (loanConfig == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(loanConfig);
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @RequestMapping(method = RequestMethod.POST, path = "/list")
    public ResponseEntity<?> getPageableLoanConfig(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(loanConfigService
            .findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }


    @RequestMapping(method = RequestMethod.GET, path = "/statusCount")
    public ResponseEntity<?> getLoanStatusCount() {
        return new RestResponseDto().successModel(loanConfigService.loanStatusCount());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public ResponseEntity<?> getLoanAll() {
        return new RestResponseDto().successModel(loanConfigService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getLoanOne(@PathVariable Long id) {
        return new RestResponseDto().successModel(loanConfigService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/status")
    public ResponseEntity<?> getLoanByStatus(@RequestBody Status status) {
        return new RestResponseDto().successModel(loanConfigService.getAllByStatus(status));
    }

    @GetMapping(value = "/all/eligibility")
    public ResponseEntity<?> getLoanConfigsForEligibility() {
        logger.debug("Request to get the Loan configs activated for eligibility.");
        final List<LoanConfig> loanConfigs = loanConfigService
            .getLoanConfigsActivatedForEligibility();
        final List<LoanConfigDto> loanConfigDtoList = eligibilityLoanConfigMapper
            .mapEntitiesToDtos(loanConfigs);
        return new RestResponseDto().successModel(loanConfigDtoList);
    }

    @GetMapping(value = "/{loanConfigId}/eligibility")
    public ResponseEntity<?> getLoanOneForEligibility(@PathVariable Long loanConfigId) {
        final LoanConfigDto loanConfigDto = eligibilityLoanConfigMapper
            .mapEntityToDto(loanConfigService.getLoanConfigActivatedForEligibility(loanConfigId));
        return new RestResponseDto().successModel(loanConfigDto);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{loanCategory}/all")
    public ResponseEntity<?> getLoanAllByLoanApprovalType(
        @PathVariable LoanApprovalType loanCategory) {
        return new RestResponseDto()
            .successModel(loanConfigService.getByLoanCategoryAndStatus(loanCategory));
    }

}
