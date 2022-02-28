package com.sb.solutions.web.loanTemplate;

import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.loanTemplate.entity.LoanTemplate;
import com.sb.solutions.api.loanTemplate.service.LoanTemplateService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.PaginationUtils;

/**
 * @author Rujan Maharjan on 2/25/2019
 */

@RestController
@RequestMapping("/v1/loan-template")
public class LoanTemplateController {

    private final LoanTemplateService loanTemplateService;

    public LoanTemplateController(
        @Autowired LoanTemplateService loanTemplateService) {
        this.loanTemplateService = loanTemplateService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveTemplate(@Valid @RequestBody LoanTemplate loanTemplate) {
        final LoanTemplate template = loanTemplateService.save(loanTemplate);

        if (template == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(template);
        }
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @RequestMapping(method = RequestMethod.POST, path = "/list")
    public ResponseEntity<?> getPageableLoanTemplate(@RequestBody SearchDto searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(loanTemplateService
            .findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }


    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public ResponseEntity<?> getLoanTemplate() {
        return new RestResponseDto().successModel(loanTemplateService.findAll());
    }

}
