package com.sb.solutions.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchy;
import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchyService;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.constant.ApiConstants;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.ApprovalType;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.dto.CadStageDto;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.service.LoanHolderService;
import com.sb.solutions.service.approvedloancaddoc.CustomerCadService;
import com.sb.solutions.service.report.CadReportService;
import com.sb.solutions.validation.constraint.CadValid;


/**
 * @author : Rujan Maharjan on  12/1/2020
 **/

@RestController
@Validated
@RequestMapping(value = ApiConstants.CAD_BASE_API)
public class CadController {

    private final LoanHolderService loanHolderService;

    private final ApprovalRoleHierarchyService approvalRoleHierarchyService;

    private final UserService userService;

    private final CadReportService reportService;

    @Qualifier("customerCadService")
    private final CustomerCadService customerCadService;

    public CadController(LoanHolderService loanHolderService,
        ApprovalRoleHierarchyService approvalRoleHierarchyService,
        UserService userService,
        CadReportService reportService,
        CustomerCadService customerCadService) {
        this.loanHolderService = loanHolderService;
        this.approvalRoleHierarchyService = approvalRoleHierarchyService;
        this.userService = userService;
        this.reportService = reportService;
        this.customerCadService = customerCadService;
    }

    @PostMapping(value = ApiConstants.UNASSIGNED_LOAN)
    public ResponseEntity<?> getUnassignedLoanForCadAdmin(
        @RequestBody Map<String, String> filterParams,
        @RequestParam("page") int page,
        @RequestParam("size") int size) {
        return new RestResponseDto().successModel(loanHolderService
            .getAllUnAssignLoanForCadAdmin(filterParams, PaginationUtils.pageable(page, size)));
    }


    @CadValid
    @PostMapping(value = ApiConstants.ASSIGN_LOAN_TO_USER)
    public ResponseEntity<?> assignLoanToUser(@RequestBody CadStageDto cadStageDto) {
        return new RestResponseDto().successModel(loanHolderService.assignLoanToUser(cadStageDto));
    }


    @PostMapping(value = ApiConstants.CAD_ACTION)
    public ResponseEntity<?> action(@RequestBody CadStageDto cadStageDto) {
         return new RestResponseDto().successModel(loanHolderService.cadAction(cadStageDto));
    }

    @GetMapping(value = ApiConstants.CAD_ROLE_LIST)
    public ResponseEntity<?> getRoleListPresentInCAD() {
        return new RestResponseDto()
            .successModel(
                approvalRoleHierarchyService.getRoles(ApprovalType.CAD, 0L));
    }

    @PostMapping(value = ApiConstants.CAD_LIST)
    public ResponseEntity<?> getCadList(
        @RequestBody Map<String, String> filterParams,
        @RequestParam("page") int page,
        @RequestParam("size") int size) {
        return new RestResponseDto().successModel(loanHolderService
            .getAllByFilterParams(filterParams, PaginationUtils.pageableWithSort(page, size,"docStatus")));
    }

    @GetMapping(value = ApiConstants.GET_BY_ID)
    public ResponseEntity<?> getCad(@PathVariable Long id) {
        return new RestResponseDto().successModel(loanHolderService.getByID(id));
    }

    @PatchMapping(ApiConstants.SAVE_CAD)
    public ResponseEntity<?> saveCAD(@RequestBody CustomerApprovedLoanCadDocumentation c) {
        return new RestResponseDto().successModel(customerCadService.save(c));
    }

    @PostMapping(path = ApiConstants.UPLOAD_FILE)
    public ResponseEntity<?> uploadOfferLetter(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("customerApprovedDocId") Long customerApprovedDocId,
        @RequestParam("offerLetterId") Long offerLetterId,
        @RequestParam(name = "type", required = true, defaultValue = "DRAFT") String type) {
        return new RestResponseDto().successModel(
            customerCadService
                .saveWithMultipartFile(multipartFile, customerApprovedDocId, offerLetterId, type));
    }


    @PostMapping(value = ApiConstants.UPLOAD_LEGAL_FILE)
    public ResponseEntity<?> cadCheckListDoc(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("customerInfoId") Long customerInfoId, @RequestParam("loanID") Long loanId,
        @RequestParam("customerApprovedDocId") Long customerApprovedDocId,
        @RequestParam("documentName") String documentName,
        @RequestParam("documentId") Long documentId

    ) {
        return new RestResponseDto().successModel(customerCadService
            .saveCadCheckListDoc(multipartFile, customerInfoId, loanId, customerApprovedDocId,
                documentName, documentId));
    }

    @PostMapping(value = ApiConstants.UPLOAD_ADDITIONAL_FILE)
    public ResponseEntity<?> cadCheckListDoc(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("cadId") Long cadId,
        @RequestParam("documentName") String documentName,
        @RequestParam("branchId") Long branchId,
        @RequestParam("customerInfoId") Long customerInfoId

    ) {
        return new RestResponseDto().
            successModel(customerCadService
                .uploadAdditionalDocument(multipartFile, cadId, documentName, branchId,
                    customerInfoId));
    }

    @CadValid
    @PostMapping(value = ApiConstants.ASSIGN_CAD_TO_USER)
    public ResponseEntity<?> assignCADToUser(@RequestBody CadStageDto cadStageDto) {
        return new RestResponseDto().successModel(loanHolderService.assignCadToUser(cadStageDto));
    }

    @PostMapping(value = ApiConstants.UPLOAD_SCC_FILE)
    public ResponseEntity<?> sccFilePath(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("cadId") Long cadId,
        @RequestParam("branchId") Long branchId,
        @RequestParam("customerInfoId") Long customerInfoId

    ) {
        return new RestResponseDto().
            successModel(customerCadService
                .sccFilePath(multipartFile, cadId, branchId,
                    customerInfoId));
    }

    @GetMapping(value = ApiConstants.GET_OFFER_LETTER_COUNT)
    public ResponseEntity<?> getCadCount() {
        return new RestResponseDto()
            .successModel(loanHolderService.getCadDocumentCount(new HashMap<>()));
    }

    @PostMapping(value = ApiConstants.POST_ADDITIONAL_DISBURSEMENT)
    public ResponseEntity<?> saveAdditionalDisbursement(
        @RequestBody CustomerApprovedLoanCadDocumentation c, @PathVariable Long roleId) {
        return new RestResponseDto()
            .successModel(loanHolderService.saveAdditionalDisbursement(c, roleId));
    }

    @GetMapping(value = ApiConstants.GET_USER_BY_BRANCH_IN_CAD)
    public ResponseEntity<?> getRoleListPresentInCAD(@PathVariable("branchId") Long branchId) {
        List<User> userList = new ArrayList<>();
        List<ApprovalRoleHierarchy> approvalRoleHierarchyList = approvalRoleHierarchyService
            .getRoles(ApprovalType.CAD, 0L);
        List<ApprovalRoleHierarchy> approvalRoleHierarchyNotMaker = approvalRoleHierarchyList
            .stream().filter(f ->
                !f.getRole().getRoleType().equals(RoleType.MAKER)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(approvalRoleHierarchyNotMaker)) {
            approvalRoleHierarchyNotMaker.sort(
                Comparator
                    .comparing(ApprovalRoleHierarchy::getRoleOrder, Comparator.reverseOrder()));
            Role role = approvalRoleHierarchyNotMaker.get(0).getRole();
            if (role.getRoleAccess().equals(RoleAccess.ALL)) {
                userList = userService.findByRoleId(role.getId());
            } else {
                userList = userService.findByRoleAndBranchId(role.getId(), branchId);
            }
        }
        userList
            .addAll(userService.findByRoleTypeAndBranchIdAndStatusActive(RoleType.MAKER, branchId));
        return new RestResponseDto()
            .successModel(
                userList.stream().filter(f->f.getStatus().equals(Status.ACTIVE)).collect(
                    Collectors.toList()));
    }

    @PostMapping(value = ApiConstants.GET_REPORT)
    public ResponseEntity<?> getReport(
        @RequestBody Map<String, String> filterParams) {
        return new RestResponseDto().successModel(reportService.reportPath(filterParams));
    }

    @GetMapping(value = ApiConstants.GET_STAT)
    public ResponseEntity<?> getStat() {
        return new RestResponseDto().successModel(loanHolderService.getStat());
    }

}
