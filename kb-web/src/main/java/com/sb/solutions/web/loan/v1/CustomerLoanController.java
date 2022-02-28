package com.sb.solutions.web.loan.v1;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.google.common.base.Preconditions;
import com.sb.solutions.api.loan.service.CustomerDocumentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.aop.CustomerLoanLog;
import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.customerRelative.entity.CustomerRelative;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.loan.entity.CadDocument;
import com.sb.solutions.api.loan.entity.CustomerDocument;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.repository.specification.CustomerLoanSpec;
import com.sb.solutions.api.loan.service.CombinedLoanService;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.LoanType;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.web.common.stage.dto.StageDto;
import com.sb.solutions.web.loan.v1.mapper.Mapper;

/**
 * @author Rujan Maharjan on 5/10/2019
 */

@RestController
@Validated
@RequestMapping(CustomerLoanController.URL)
public class CustomerLoanController {

    static final String URL = "/v1/Loan-customer";

     private static final Logger logger = LoggerFactory.getLogger(CustomerLoanController.class);

    private final CustomerLoanService service;
    private final UserService userService;
    private final Mapper mapper;
    private final CombinedLoanService combinedLoanService;
    private final CustomerDocumentService customerDocumentService;

    @Value("${bank.affiliateId}")
    private String affiliateId;

    public CustomerLoanController(
            CustomerLoanService service,
            Mapper mapper,
            UserService userService,
            CombinedLoanService combinedLoanService,
            CustomerDocumentService customerDocumentService) {

        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
        this.combinedLoanService = combinedLoanService;
        this.customerDocumentService = customerDocumentService;
    }

    @PostMapping(value = "/action")
    public ResponseEntity<?> loanAction(@Valid @RequestBody StageDto actionDto) {
        final CustomerLoan c = mapper
            .actionMapper(actionDto, service.findOne(actionDto.getCustomerLoanId()),
                userService.getAuthenticatedUser());
        service.sendForwardBackwardLoan(c);
        return new RestResponseDto().successModel(actionDto);
    }

    @PostMapping(value = "/action/combined")
    public ResponseEntity<?> loanAction(@Valid @RequestBody List<StageDto> actionDtoList,
        @RequestParam boolean stageSingle) {
        User user = userService.getAuthenticatedUser();
        List<CustomerLoan> loans = actionDtoList.stream()
            .map(dto -> mapper.actionMapper(dto, service.findOne(dto.getCustomerLoanId()), user))
            .collect(Collectors.toList());
        Long combinedLoanId = loans.get(0).getCombinedLoan().getId();
        // remove from combined loan if loans are staged individually
        // or loans are combined and approved
        boolean removeCombinedApproved = stageSingle || actionDtoList.stream()
            .anyMatch(a -> a.getDocAction().equals(DocAction.APPROVED));
        boolean removeCombinedReject = stageSingle || actionDtoList.stream()
                .anyMatch(a ->a.getDocAction().equals(DocAction.REJECT));
        boolean removeCombinedClosed = stageSingle || actionDtoList.stream()
                .anyMatch(a ->a.getDocAction().equals(DocAction.CLOSED));
        if (removeCombinedApproved || removeCombinedReject || removeCombinedClosed) {
            loans.forEach(l -> l.setCombinedLoan(null));
        }
        service.sendForwardBackwardLoan(loans);
        // remove unassociated combined loan entry
        if (removeCombinedApproved || removeCombinedReject || removeCombinedClosed) {
            combinedLoanService.deleteById(combinedLoanId);
        }
        return new RestResponseDto().successModel(actionDtoList);
    }

    @CustomerLoanLog(Activity.LOAN_UPDATE)
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CustomerLoan customerLoan) {

        logger.debug("saving Customer Loan {}", customerLoan);
        if (customerLoan.getId() == null) {
            if (customerLoan.getProposal() == null) {
                return new RestResponseDto().failureModel("Proposal can not be null");
            } else {
                if (customerLoan.getProposal().getProposedLimit() == null) {
                    return new RestResponseDto().failureModel("Proposal limit can not be null");
                }
            }
        }
        return new RestResponseDto().successModel(service.saveLoan(customerLoan, userService.getAuthenticatedUser()));
    }

    @PostMapping("/close-renew-customer-loan")
    public ResponseEntity<?> closeRenew(@Valid @RequestBody CustomerLoan customerLoan) {
        logger.debug("saving Customer Loan {}", customerLoan);
        return new RestResponseDto().successModel(service.renewCloseEntity(customerLoan));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.findOne(id));
    }


    @GetMapping("/{id}/delete")
    public ResponseEntity<?> delByIdRoleMaker(@PathVariable("id") Long id) {
        logger.info("deleting Customer Loan {}", id);
        return new RestResponseDto()
            .successModel(service.delCustomerLoan(id));
    }

    @PostMapping("/status")
    public ResponseEntity<?> getfirst5ByDocStatus(@RequestBody CustomerLoan customerLoan) {
        logger.debug("getByDocStatus Customer Loan {}", customerLoan);
        return new RestResponseDto().successModel(
            service.getFirst5CustomerLoanByDocumentStatus(customerLoan.getDocumentStatus()));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> getAllByPagination(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        if (ProductUtils.CUSTOMER_BASE_LOAN) {
            return new RestResponseDto()
                .successModel(
                    service.getLoanByCustomerInfo(searchDto, PaginationUtils.pageable(page, size)));
        } else {
            return new RestResponseDto()
                .successModel(
                    service.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));

        }
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllBySearch(@RequestBody Object searchDto) {
        return new RestResponseDto().successModel(service.findAll(searchDto));
    }

    @GetMapping(value = "/statusCount")
    public ResponseEntity<?> countLoanStatus() {
        return new RestResponseDto().successModel(service.statusCount());
    }

    @GetMapping(value = "/proposed-amount")
    public ResponseEntity<?> getProposedAmount(@RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        return new RestResponseDto().successModel(service.proposedAmount(startDate, endDate));
    }

    @GetMapping(value = "/loan-amount/{id}")
    public ResponseEntity<?> getProposedAmountByBranch(@PathVariable Long id,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        return new RestResponseDto().successModel(service.proposedAmountByBranch(id, startDate,
            endDate));
    }

    @GetMapping(value = "/searchByCitizenship/{number}")
    public ResponseEntity<?> getLoansByCitizenship(
        @PathVariable("number") String citizenshipNumber) {
        logger.info("GET:/searchByCitizenship/{}", citizenshipNumber);
        return new RestResponseDto()
            .successModel(service.getByCitizenshipNumber(citizenshipNumber));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/catalogue")
    public ResponseEntity<?> getCatalogues(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(service.getCatalogues(searchDto, PaginationUtils.pageable(page, size)));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/committee-pull")
    public ResponseEntity<?> getCommitteePull(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {

        if (ProductUtils.CUSTOMER_BASE_LOAN) {
            return new RestResponseDto()
                .successModel(
                    service.getLoanByCustomerInfoCommitteePULL(searchDto,
                        PaginationUtils.pageable(page, size)));
        } else {
            return new RestResponseDto()
                .successModel(
                    service.getCommitteePull(searchDto, PaginationUtils.pageable(page, size)));

        }

    }

    @GetMapping(path = "/stats")
    public ResponseEntity<?> getStats(@RequestParam(value = "branchId") Long branchId,
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) throws ParseException {
        logger.debug("REST request to get the statistical data about the loans.");
        return new RestResponseDto().successModel(mapper.toBarchartDto(service.getStats(branchId,
            startDate, endDate)));
    }

    @GetMapping(path = "/check-user-customer-loan/{id}")
    public ResponseEntity<?> chkUserContainCustomerLoan(@PathVariable Long id) {
        logger.debug("REST request to get the check data about the user.");
        return new RestResponseDto().successModel(service.chkUserContainCustomerLoan(id));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/csv")
    public ResponseEntity<?> csv(@RequestBody Object searchDto) {
        return new RestResponseDto().successModel(service.csv(searchDto));
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadLoanFile(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("loanId") Long loanId,
        @RequestParam("documentName") String documentName,
        @RequestParam("documentId") Long documentId,
        @RequestParam("loanHolderId") Long loanHolderId,
        @RequestParam("customerType") String customerType,
        @RequestParam(name = "actualLoanId", required = false, defaultValue = "") String actualLoanId,
        @RequestParam(name = "action", required = false, defaultValue = "new") String action) {
        Preconditions.checkNotNull(loanHolderId, "Loan Holder cannot be null");
        Preconditions.checkNotNull(customerType, "CustomerType cannot be null");
        Preconditions.checkNotNull(loanId, "LoanConfig cannot be null");

        Long branchId;
        List<Branch> branches = userService.getAuthenticatedUser().getBranch();
        if (branches.size() != 1) {
            throw new ServiceValidationException("You do not have permission to upload file!");
        } else {
            branchId = branches.get(0).getId();
        }
        Preconditions.checkNotNull(branchId, "Branch cannot be null");

        CustomerDocument customerDocument = new CustomerDocument();
        Document document = new Document();
        document.setId(documentId);
        customerDocument.setDocument(document);

        if (StringUtils.isEmpty(actualLoanId)) {
            String uploadPath = new PathBuilder(UploadDir.initialDocument)
                    .buildTempLoanDocumentUploadBasePathForNewLoan(loanHolderId,
                            branchId,
                            customerType,
                            action,
                            loanId);
            logger.info("File Upload Path {}", uploadPath);
            ResponseEntity<?> responseEntity = FileUploadUtils
                    .uploadFile(multipartFile, uploadPath, documentName);
            customerDocument
                    .setDocumentPath(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        } else {
            String uploadPath;
            CustomerLoan customerLoan = service.findOne(Long.parseLong(actualLoanId));
            Optional<CustomerDocument> optionalCustomerDocument = customerLoan.getCustomerDocument().stream().filter(d -> d.getDocument().getName().equalsIgnoreCase(documentName)).findAny();
            if (optionalCustomerDocument.isPresent()) {
                CustomerDocument customerDocument1 = optionalCustomerDocument.get();
                String tempPath = customerDocument1.getDocumentPath();
                uploadPath = tempPath.substring(0, tempPath.lastIndexOf("/")) + "/";
                customerDocument.setId(customerDocument1.getId());
                int version = customerDocument1.getVersion();
                customerDocument.setVersion(version);
            } else {
                uploadPath = new PathBuilder(UploadDir.initialDocument)
                        .buildLoanDocumentUploadBasePathWithLoanId(loanHolderId,
                                branchId,
                                customerType,
                                action,
                                loanId, actualLoanId);
            }
            logger.info("File Upload Path {}", uploadPath);
            ResponseEntity<?> responseEntity = FileUploadUtils
                    .uploadFile(multipartFile, uploadPath, documentName);
            customerDocument
                    .setDocumentPath(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        }
        return new RestResponseDto().successModel(customerDocument);
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
                service.getIssuedOfferLetter(searchDto, PaginationUtils.pageable(page, size)));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getLoanByCustomerId(@PathVariable("id") Long id) {
        logger.info("getting Customer Loan {}", id);
        return new RestResponseDto()
            .successModel(service.getLoanByCustomerId(id));
    }

    @PostMapping("/customer-kyc")
    public ResponseEntity<?> getLoanByCustomerAsKyc(
        @RequestBody CustomerRelative customerRelative) {
        logger.info("getting Customer Loan by Kyc {}", customerRelative);
        return new RestResponseDto()
            .successModel(service.getLoanByCustomerKycGroup(customerRelative));
    }

    @PostMapping("/customer-list")
    public ResponseEntity<?> getCustomerFromCustomerLoan(
        @RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        logger.info("getting Customer  from Loan /customer-kyc {}", searchDto);
        return new RestResponseDto()
            .successModel(service
                .getCustomerFromCustomerLoan(searchDto, PaginationUtils.pageable(page, size)));
    }

    @PostMapping("/customer-guaranter")
    public ResponseEntity<?> getLoanByCustomerAsGuaranter(
        @RequestBody Guarantor guarantor) {
        logger.info("getting Customer Loan by guarantor {}", guarantor);
        return new RestResponseDto()
            .successModel(service.getLoanByCustomerGuarantor(guarantor));
    }

    @GetMapping("/loan-holder/{id}")
    public ResponseEntity<?> getLoanByLoanHolderId(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.getLoanByLoanHolderId(id));
    }

    @GetMapping("/final-loan-list/{id}")
    public ResponseEntity<?> getFinalLoanById(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.getFinalUniqueLoansById(id));
    }

    @GetMapping("/loan-holder/{id}/for-combine")
    public ResponseEntity<?> getInitialLoanByLoanHolderId(@PathVariable("id") Long id) {
        Map<String, String> filter = new HashMap<>();
        User u = userService.getAuthenticatedUser();
        String branchAccess = userService.getRoleAccessFilterByBranch().stream()
            .map(Object::toString).collect(Collectors.joining(","));
        filter.put("branchIds", branchAccess);
        filter.put("currentUserRole", u.getRole() == null ? null : u.getRole().getId().toString());
        filter.put("toUser", u.getId().toString());
        filter.put("loanHolderId", String.valueOf(id));
        filter.put(CustomerLoanSpec.FILTER_BY_DOC_STATUS, "initial");
        List<CustomerLoan> loans = new ArrayList<>(service.findAllBySpec(filter));
        filter
            .put(CustomerLoanSpec.FILTER_BY_DOC_STATUS, DocStatus.PENDING.toString().toUpperCase());
        loans.addAll(service.findAllBySpec(filter));
        return new RestResponseDto().successModel(loans);
    }


    @PostMapping("/customer-group")
    public ResponseEntity<?> getLoanByCustomerGroup(
        @RequestBody CustomerGroup customerGroup) {
        logger.info("getting Customer Loan by CustomerGroup {}", customerGroup);
        return new RestResponseDto()
            .successModel(service.getLoanByCustomerGroup(customerGroup));
    }

    @PostMapping("/cad-document")
    public ResponseEntity<?> saveCadDocumentLoan(@RequestParam Long loanId,
        @RequestBody List<CadDocument> customerDocuments, @RequestParam String data) {
        return new RestResponseDto()
            .successModel(service.saveCadLoanDocument(loanId, customerDocuments, data));
    }

    @PostMapping("/cbs")
    public ResponseEntity<?> saveCbsNumber(
        @RequestBody CustomerLoan customerLoan) {
        return new RestResponseDto()
            .successModel(service.saveCbsNumbers(customerLoan));
    }

    @PostMapping("/cad-document/upload")
    public ResponseEntity<?> uploadLoanCadFile(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("documentName") String documentName,
        @RequestParam("documentId") Long documentId,
        @RequestParam("customerLoanId") Long customerLoanId) {
        CadDocument cadDocument = new CadDocument();
        Document document = new Document();
        document.setId(documentId);
        cadDocument.setDocument(document);
        CustomerLoan customerLoan = service.findOne(customerLoanId);
        String uploadPath = new PathBuilder(UploadDir.initialDocument)
            .buildCadLoanDocumentUploadBasePathWithId(customerLoan.getLoanHolder().getId(),
                customerLoan.getBranch().getId(),
                customerLoan.getLoanHolder().getCustomerType().name(),
                actionType(customerLoan.getLoanType()),
                customerLoan.getLoan().getId());
        logger.info("File Upload Path {}", uploadPath);
        ResponseEntity<?> responseEntity = FileUploadUtils
            .uploadFile(multipartFile, uploadPath, documentName);
        cadDocument
            .setDocumentPath(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        return new RestResponseDto().successModel(cadDocument);
    }

    @GetMapping("/customer-editable/{id}")
    public ResponseEntity<?> isCustomerEditable(@PathVariable Long id) {
        return new RestResponseDto().successModel(service.checkCustomerIsEditable(id));
    }

    @GetMapping("/change-loan/customer-loan-id/{id}/loan-config-id/{lId}")
    public ResponseEntity<?> performChangeLoan(@PathVariable Long id, @PathVariable Long lId) {
        service.changeLoan(id, lId);
        return new RestResponseDto().successModel("SUCCESS");
    }

    @DeleteMapping("/delete-loan/{id}")
    public ResponseEntity<?> deleteLoanByAdminAndMaker(@PathVariable Long id) {
        logger.info("Delete Loan {}", id);
        service.deleteLoanByMakerAndAdmin(id);
        return new RestResponseDto().successModel("SUCCESS");
    }

    @PostMapping("/re-initiate-loan")
    public ResponseEntity<?> reInitiateLoan(@Valid @RequestBody StageDto stageDto) {
        User currentUser = userService.getAuthenticatedUser();
        if (currentUser.getRole().getRoleType() == RoleType.ADMIN
            || currentUser.getRole().getRoleType() == RoleType.MAKER) {
            logger.info("Re-initiate Loan {}", stageDto.getCustomerLoanId());
            stageDto.setDocAction(DocAction.RE_INITIATE);
            stageDto.setDocumentStatus(DocStatus.PENDING);
            final CustomerLoan c = mapper
                .actionMapper(stageDto, service.findOne(stageDto.getCustomerLoanId()),
                    currentUser);
            c.setModifiedBy(currentUser.getId());
            c.setLastModifiedAt(new Date());
            service.sendForwardBackwardLoan(c);
            // service.reInitiateRejectedLoan(stageDto.getCustomerLoanId(), stageDto.getComment());
            return new RestResponseDto().successModel("SUCCESS");
        } else {
            return new RestResponseDto()
                .failureModel(
                    "Failure: You are not authorized!!!");
        }
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

    @PostMapping("/delete-document/")
    public ResponseEntity deleteCustomerDocFromSystem(@RequestBody String path) {
        return new RestResponseDto()
                .successModel(customerDocumentService.deleteCustomerDocFromSystem(path));
    }
}
