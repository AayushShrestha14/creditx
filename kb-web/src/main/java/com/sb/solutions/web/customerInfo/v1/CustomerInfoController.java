package com.sb.solutions.web.customerInfo.v1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.validation.Valid;

import com.google.common.base.Preconditions;
import com.sb.solutions.core.validation.constraint.MultipleFileFormatValid;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.customer.service.CustomerGeneralDocumentService;
import com.sb.solutions.api.customer.service.CustomerInfoService;
import com.sb.solutions.api.customerActivity.aop.Activity;
import com.sb.solutions.api.customerActivity.aop.CustomerActivityLog;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.service.CustomerLoanService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.core.utils.string.StringUtil;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.web.customerInfo.v1.dto.CustomerTransferDTO;
import com.sb.solutions.web.loan.v1.mapper.Mapper;

/**
 * @author : Rujan Maharjan on  8/10/2020
 **/
@RestController
@Validated
@RequestMapping(CustomerInfoController.URL)
public class CustomerInfoController {

    public static final String URL = "/v1/customer-info";
    private final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);
    private final CustomerInfoService customerInfoService;
    private final CustomerGeneralDocumentService customerGeneralDocumentService;
    private final UserService userService;
    private final CustomerLoanService customerLoanService;
    private final Mapper mapper;


    @Autowired
    public CustomerInfoController(
        CustomerInfoService customerInfoService,
        CustomerGeneralDocumentService customerGeneralDocumentService,
        UserService userService,
        CustomerLoanService customerLoanService, Mapper mapper) {
        this.customerInfoService = customerInfoService;
        this.customerGeneralDocumentService = customerGeneralDocumentService;
        this.userService = userService;
        this.customerLoanService = customerLoanService;
        this.mapper = mapper;
    }

    @PostMapping("/list")
    public ResponseEntity<?> getPageable(@RequestBody Map<String, String> searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(customerInfoService.
                getCustomerListDto(searchDto, PaginationUtils.pageable(page,size)));
    }

    @CustomerActivityLog(Activity.CUSTOMER_UPDATE)
    @PostMapping("/{customerInfoId}/{template}")
    public ResponseEntity<?> saveCustomerLoanInfo(@RequestBody Object loanInfo,
        @PathVariable("customerInfoId") Long customerInfoId,
        @PathVariable("template") String template) {
        return new RestResponseDto()
            .successModel(
                customerInfoService.saveLoanInformation(loanInfo, customerInfoId, template));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerInfoByID(
        @PathVariable("id") Long id) {
        CustomerInfo customerInfo = customerInfoService.findOne(id).get();
        List<CustomerGeneralDocument> generalDocuments = customerGeneralDocumentService
            .findByCustomerInfoId(id);
        if (!generalDocuments.isEmpty()) {
            customerInfo.setCustomerGeneralDocuments(generalDocuments);
        }
        return new RestResponseDto()
            .successModel(customerInfo);
    }

    @PostMapping("/customer")
    public ResponseEntity getCustomerByTypeIdNumberIdTypeRegDate(
        @RequestBody CustomerInfo customerInfo) {
        final CustomerInfo customerInfo1 = customerInfoService
            .findByCustomerTypeAndIdNumberAndIdRegPlaceAndIdTypeAndIdRegDate(
                customerInfo.getCustomerType(), customerInfo.getIdNumber(),
                customerInfo.getIdRegPlace(), customerInfo.getIdType(),
                customerInfo.getIdRegDate());
        if (ObjectUtils.isEmpty(customerInfo1)) {
            return new RestResponseDto().failureModel("no Customer Found");
        }
        return new RestResponseDto().successModel(
            customerInfo1
        );
    }

    @PostMapping("/upload-photo")
    public ResponseEntity profilePicUploader(
        @RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("customerInfoId") Long customerInfoId,
        @RequestParam("name") String name,
        @RequestParam("branch") String branch,
        @RequestParam("customerType") String customerType) {
        Preconditions.checkNotNull(name.equals("null") ? null
                : (StringUtils.isEmpty(name) ? null : name),
            "Customer Name is required to upload file.");
        Preconditions.checkNotNull(branch.equals("null") ? null
                : (StringUtils.isEmpty(branch) ? null : branch),
            "Branch Name is required to upload file.");
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");

        String formattedDateTime = currentDateTime.format(formatter);
        final Optional<CustomerInfo> customerInfo = customerInfoService.findOne(customerInfoId);
        if (!customerInfo.isPresent()) {
            throw new ServiceValidationException("Invalid customer id");
        }
        String uploadPath = new PathBuilder(UploadDir.initialDocument)
            .buildCustomerInfoBasePathWithId(customerInfoId, customerInfo.get().getBranch().getId(),
                customerType);

        ResponseEntity responseEntity = FileUploadUtils
            .uploadFile(multipartFile, uploadPath,
                formattedDateTime + "-" + StringUtil
                    .getStringWithoutWhiteSpaceAndWithCapitalize(name.replaceAll("\\/","-"))
                    .toLowerCase());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Object body = responseEntity.getBody();
            RestResponseDto restResponseDto = new RestResponseDto();
            if (!ObjectUtils.isEmpty(body)) {
                BeanUtils.copyProperties(body, restResponseDto);
                String path = (String) restResponseDto.getDetail();
                if (customerInfo.isPresent()) {
                    logger.info("updating profile picture::{}", customerInfo.get().getName());
                    CustomerInfo c = customerInfo.get();
                    c.setProfilePic(path);
                    customerInfoService.save(c);
                }
            }
        }
        return responseEntity;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") @MultipleFileFormatValid List<MultipartFile> multipartFile,
                                    @RequestParam("customerName") String name,
                                    @RequestParam("documentName") String documentName,
                                    @RequestParam("customerInfoId") Long id,
                                    @RequestParam("customerType") String customerType,
                                    @RequestParam("folderName") String folderName,
                                    @RequestParam("uploadedDoc") String uploadedDoc
    ) {
        List<String> savedFileName = new ArrayList<>();
        final String branchName = userService.getAuthenticatedUser().getBranch().get(0).getName();
        Preconditions.checkNotNull(name.equals("undefined") || name.equals("null") ? null
                : (StringUtils.isEmpty(name) ? null : name), "Customer Name "
                + "is required to upload file.");
        Preconditions
                .checkNotNull(documentName.equals("undefined") || documentName.equals("null") ? null
                        : (StringUtils.isEmpty(documentName) ? null : documentName), "Doc Name "
                        + "is required to upload file.");
        documentName = documentName.replaceAll("\\\\", "-");
        documentName = documentName.replace("/", "-");

        Optional<CustomerInfo> customerInfo = customerInfoService.findOne(id);
        if(uploadedDoc != null && !uploadedDoc.isEmpty()){
            savedFileName = new ArrayList<>(Arrays.asList(uploadedDoc.split("\\s*,\\s*")));
        };
        if (!customerInfo.isPresent()) {
            throw new ServiceValidationException("Invalid customer id");
        }
        String basicPath = new PathBuilder(UploadDir.initialDocument)
                .buildCustomerInfoBasePathWithId(id, customerInfo.get().getBranch().getId(),
                        customerType);
        ResponseEntity<?> responseEntity = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.mm.dd.HH.mm.ss.SSS");
        Random r = new Random();
        for (int i = 0; i < multipartFile.size(); i++) {
            MultipartFile file = multipartFile.get(i);
            String stringDate = formatter.format(new Date());
            Date date = null;
            try {
                date = (Date) formatter.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long timeStamp = date.getTime();
            String fileName = documentName.concat("-").concat(String.valueOf(timeStamp)).concat("-").concat(String.valueOf(r.nextInt(9999)));;
            String uploadPath = new StringBuilder(basicPath)
                    .append(folderName)
                    .append("/")
                    .toString();
            logger.info("File Upload Path {}", uploadPath);
            responseEntity = FileUploadUtils
                    .uploadFile(file, uploadPath, fileName);
            savedFileName.add(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        }
        return new RestResponseDto()
                .successModel(savedFileName);
    }


    @PostMapping("/csv")
    public ResponseEntity<?> getDownload(@RequestBody Object searchDto) {
        return new RestResponseDto()
            .successModel(customerInfoService.csv(searchDto));
    }

    @PostMapping("/update-cbs")
    public ResponseEntity<?> updateCbs(@RequestBody CustomerInfo customerInfo) {
        return new RestResponseDto()
            .successModel(
                customerInfoService.updateObligor(customerInfo.getObligor(), customerInfo.getId()));

    }

    @PatchMapping("/update-nep-data/{id}")
    public ResponseEntity<?> UpdateNepaliConfigData(@RequestBody String nepData , @PathVariable Long id) {
        return new RestResponseDto()
                .successModel(
                        customerInfoService.updateNepaliConfigData(nepData, id));

    }

    @PostMapping("/transfer-customer-other-branch")
    public ResponseEntity<?> transferCustomerWithLoansToOtherBranch(
        @Valid @RequestBody CustomerTransferDTO transferDto) {
        User currentUser = userService.getAuthenticatedUser();
        if (transferDto.getDocAction() == DocAction.TRANSFER) {
            if (transferDto.getCustomerInfoId() != null && transferDto.getToUserId() != null
                && transferDto.getToBranchId() != null) {
                List<CustomerLoan> customerLoans = customerLoanService
                    .getAllLoansByLoanHolderId(transferDto.getCustomerInfoId());
                customerLoans.forEach(customerLoan -> {
                    final CustomerLoan loan = mapper
                        .transferBranchMapper(transferDto, customerLoan, currentUser);
                    CustomerLoan loan1 = customerLoanService.save(loan);
                    customerLoanService
                        .transferLoanToOtherBranch(loan1, transferDto.getToBranchId(),
                            currentUser);
                });
                customerInfoService.updateCustomerBranch(transferDto.getCustomerInfoId(),
                    transferDto.getToBranchId());
                return new RestResponseDto()
                    .successModel("SUCCESS");
            } else {
                return new RestResponseDto()
                    .failureModel("Failure : Some data missing.");
            }
        } else {
            return new RestResponseDto()
                .failureModel(
                    "Failure: Action other than transfer detected!!!");
        }
    }


}
