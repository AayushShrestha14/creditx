package com.sb.solutions.web.document.v1;

import java.util.List;
import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.document.entity.LoanCycle;
import com.sb.solutions.api.document.service.DocumentService;
import com.sb.solutions.api.document.service.LoanCycleService;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanServiceImpl;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.web.document.v1.dto.DocumentDto;
import com.sb.solutions.web.document.v1.mapper.DocumentMapper;
import edu.emory.mathcs.backport.java.util.Arrays;


@RestController
@RequestMapping(value = "/v1/document")
public class DocumentController {

    private final DocumentService documentService;
    private final LoanCycleService loanCycleService;
    private final DocumentMapper documentMapper;
    private final CustomerLoanServiceImpl customerLoanService;

    public DocumentController(
            @Autowired DocumentService documentService,
            @Autowired LoanCycleService loanCycleService,
            @Autowired DocumentMapper documentMapper,
            CustomerLoanServiceImpl customerLoanService) {
        this.documentService = documentService;
        this.loanCycleService = loanCycleService;
        this.documentMapper = documentMapper;
        this.customerLoanService = customerLoanService;
    }

    @PostMapping
    public ResponseEntity<?> addDocument(@Valid @RequestBody DocumentDto documentDto) {

        final Document doc = documentService.save(documentMapper.mapDtoToEntity(documentDto));

        if (doc != null) {
            return new RestResponseDto().successModel(doc);
        } else {
            return new RestResponseDto().failureModel("Error Occurred");
        }
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAllByPagination(@RequestBody SearchDto searchDto,
                                                @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
                documentService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }


    @GetMapping(value = "/cycle/{cycleId}/status/{statusValue}")
    public ResponseEntity<?> getByCycleContaining(@PathVariable Long cycleId,
                                                  @PathVariable String statusValue) {
        return new RestResponseDto()
                .successModel(documentService.getByCycleContainingAndStatus(cycleId, statusValue));
    }

    @GetMapping(value = "/lifeCycle")
    public ResponseEntity<?> getLifeCycle() {
        return new RestResponseDto().successModel(loanCycleService.findAll());
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> getCount() {
        return new RestResponseDto().successModel(documentService.documentStatusCount());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "loanCycleId", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)")})
    @PostMapping(value = "/saveList")
    public ResponseEntity<?> saveList(@RequestBody List<Long> integers,
                                      @RequestParam("loanCycleId") long loanCycleId) {
        LoanCycle loanCycle = loanCycleService.findOne(loanCycleId);
        return new RestResponseDto().successModel(documentService.saveList(integers, loanCycle));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(documentService.findAll());
    }

    @GetMapping(value = "/status/{statusValue}")
    public ResponseEntity<?> getAllByStatus(@PathVariable String statusValue) {
        return new RestResponseDto().successModel(documentService.getByStatus(statusValue));
    }

    @PostMapping(value = "/downloadDoc/{id}")
    public ResponseEntity<?> downloadDoc(@RequestBody String path, @PathVariable Long id) {
        String loanPath = String.join("/",
                (Arrays.asList(path.split("/"))
                        .subList(0, Arrays.asList(path.split("/"))
                                .size() - 1)));
        final CustomerLoan customerLoan = customerLoanService.findOne(id);
        String action = actionType(customerLoan.getLoanType());

        String customerLoanDocumentPath = new PathBuilder(UploadDir.initialDocument)
                .buildLoanDocumentUploadBasePathWithLoanId(customerLoan.getLoanHolder().getId(),
                        customerLoan.getBranch().getId(),
                        customerLoan.getLoanHolder().getCustomerType().name(),
                        action,
                        customerLoan.getLoan().getId(),
                        customerLoan.getId().toString());

        String customerPath = new PathBuilder(UploadDir.initialDocument)
                .buildCustomerInfoBasePathWithId(customerLoan.getLoanHolder().getId(),
                        customerLoan.getLoanHolder().getBranch().getId(),
                        customerLoan.getLoanHolder().getCustomerType().name());


        String cadDocumentPath = null;
        if (!ObjectUtils.isEmpty(customerLoan.getCadDocument()) && ProductUtils.CAD_LITE_VERSION){
             cadDocumentPath = new PathBuilder(UploadDir.initialDocument)
                .buildCadLoanDocumentUploadBasePathWithId(customerLoan.getLoanHolder().getId(),
                    customerLoan.getBranch().getId(),
                    customerLoan.getLoanHolder().getCustomerType().name(),
                    action,
                    customerLoan.getLoan().getId());
        }


        return new RestResponseDto()
                .successModel(documentService.downloadAllDoc(customerPath, customerLoanDocumentPath, cadDocumentPath));
    }

    private String actionType(LoanType loanType) {
        switch (loanType) {
            case NEW_LOAN:
                return "NEW";
            case RENEWED_LOAN:
                return "RENEW";
            case CLOSURE_LOAN:
                return "CLOSE";
            case ENHANCED_LOAN:
                return "ENHANCE";
            case FULL_SETTLEMENT_LOAN:
                return "FULL_SETTLEMENT";
            case PARTIAL_SETTLEMENT_LOAN:
                return "PARTIAL_SETTLEMENT";
            default:
                return "";
        }
    }

}
