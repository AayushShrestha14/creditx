package com.sb.solutions.web.loan.v1;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchy;
import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchyService;
import com.sb.solutions.api.loan.service.CustomerOfferLetterPathService;
import com.sb.solutions.core.dto.SearchDto;
import com.sb.solutions.core.utils.ApprovalType;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.web.loan.v1.dto.AssignOfferLetter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.loan.entity.CustomerOfferLetter;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.loan.service.CustomerOfferService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.common.stage.mapper.OfferLetterStageMapper;

import javax.validation.Valid;
import java.util.List;



@RestController
@Validated
@RequestMapping(CustomerOfferController.URL)
public class CustomerOfferController {

    static final String URL = "/v1/customer-offer-letter";

    private static final Logger logger = LoggerFactory.getLogger(CustomerLoanController.class);

    private final CustomerOfferService customerOfferService;

    private final CustomerLoanService customerLoanService;

    private final OfferLetterStageMapper offerLetterStageMapper;

    private final ApprovalRoleHierarchyService approvalRoleHierarchyService;

    private final CustomerOfferLetterPathService customerOfferLetterPathService;


    private UserService userService;

    public CustomerOfferController(
            @Autowired CustomerOfferService customerOfferService,
            @Autowired CustomerLoanService customerLoanService,
            @Autowired UserService userService,
            @Autowired OfferLetterStageMapper offerLetterStageMapper,
            ApprovalRoleHierarchyService approvalRoleHierarchyService, CustomerOfferLetterPathService customerOfferLetterPathService) {
        this.customerOfferService = customerOfferService;
        this.customerLoanService = customerLoanService;
        this.userService = userService;
        this.offerLetterStageMapper = offerLetterStageMapper;

        this.approvalRoleHierarchyService = approvalRoleHierarchyService;
        this.customerOfferLetterPathService = customerOfferLetterPathService;
    }

    @PostMapping(path = "/uploadFile")
    public ResponseEntity<?> uploadOfferLetter(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
                                               @RequestParam("customerLoanId") Long customerLoanId,
                                               @RequestParam("offerLetterId") Long offerLetterId,
                                               @RequestParam(name = "type", required = true, defaultValue = "DRAFT") String type) {


        logger.info("uploading offer letter{}", multipartFile.getOriginalFilename());
        return new RestResponseDto().successModel(
                customerOfferService
                        .saveWithMultipartFile(multipartFile, customerLoanId, offerLetterId, type));
    }

    @PostMapping(path = "/action")
    public ResponseEntity<?> offerLetterAction(@RequestBody StageDto stageDto) {
        final CustomerOfferLetter tempCustomerOfferLetter = customerOfferService.findByCustomerLoanId(stageDto.getCustomerLoanId());
        final CustomerOfferLetter customerOfferLetter = offerLetterStageMapper
                .actionMapper(stageDto,
                        tempCustomerOfferLetter,
                        userService.getAuthenticatedUser(), tempCustomerOfferLetter.getCustomerLoan().getBranch().getId());
        return new RestResponseDto().successModel(customerOfferService.action(customerOfferLetter));
    }

    @PostMapping
    public ResponseEntity<?> initialSave(@RequestBody CustomerOfferLetter customerOfferLetter) {
        return new RestResponseDto().successModel(
                customerOfferService.save(customerOfferLetter));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return new RestResponseDto().successModel(
                customerOfferService.findOne(id));
    }

    @GetMapping(path = "/customer-loan/{id}")
    public ResponseEntity<?> getByCustomerLoanId(@PathVariable Long id) {
        return new RestResponseDto().successModel(
                customerOfferService.findByCustomerLoanId(id));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping(value = "/issue-offer-letter")
    public ResponseEntity<?> getIssuedOfferLetter(@RequestBody Object searchDto,
                                                  @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
                .successModel(
                        customerOfferService
                                .getIssuedOfferLetter(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/cad-role-list")
    public ResponseEntity<?> getRoleListPresentInCAD() {
        return new RestResponseDto()
                .successModel(
                        approvalRoleHierarchyService.getRoles(ApprovalType.CAD, 0L));
    }

    @PostMapping(value = "/assign")
    public ResponseEntity<?> assignOfferLetter(@Valid @RequestBody AssignOfferLetter assignOfferLetter) {
        return new RestResponseDto()
                .successModel(
                        customerOfferService.assignOfferLetter(assignOfferLetter.getCustomerLoanId(), assignOfferLetter.getUserId(), assignOfferLetter.getRoleId()));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.")})
    @PostMapping(value = "/assigned-offer-letter")
    public ResponseEntity<?> getAssignedOfferLetter(@RequestBody Object searchDto,
                                                    @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
                .successModel(
                        customerOfferService.getAssignedOfferLetter(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "/stat")
    public ResponseEntity<?> userPostApprovalDocumentStat() {
        return new RestResponseDto().successModel(customerOfferService.userPostApprovalDocStat());
    }

    @PostMapping(value = "/user-list")
    public ResponseEntity<?> getUserListForFilter(@RequestBody SearchDto searchDto) {
        final List<ApprovalRoleHierarchy> approvalRoleHierarchyList = approvalRoleHierarchyService.getRoles(ApprovalType.CAD, 0L);
        return new RestResponseDto()
                .successModel(customerOfferService.getUserListForFilter(approvalRoleHierarchyList, searchDto)
                );
    }

    @PostMapping(value = "/approve-partial")
    public ResponseEntity<?> approvePartialDocument(@RequestBody List<Long> ids) {
        return new RestResponseDto()
                .successModel(customerOfferLetterPathService.updateApproveStatusAndApprovedBy(ids));
    }

}
