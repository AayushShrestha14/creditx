package com.sb.solutions.web.customerGeneralDocument.v1;

import com.google.common.base.Preconditions;
import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.api.customer.service.CustomerGeneralDocumentService;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.document.service.DocumentService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.core.validation.constraint.MultipleFileFormatValid;
import com.sb.solutions.web.customerInfo.v1.CustomerInfoController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author : Rujan Maharjan on  8/25/2020
 **/

@RestController
@Validated
@RequestMapping(CustomerGeneralDocumentController.URL)
public class CustomerGeneralDocumentController {

    public static final String URL = "/v1/customer-general-document";
    private final Logger logger = LoggerFactory.getLogger(CustomerInfoController.class);

    private final CustomerGeneralDocumentService customerGeneralDocumentService;
    private final UserService userService;
    private final DocumentService documentService;

    public CustomerGeneralDocumentController(
            CustomerGeneralDocumentService customerGeneralDocumentService,
            UserService userService, DocumentService documentService) {
        this.customerGeneralDocumentService = customerGeneralDocumentService;
        this.userService = userService;
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody CustomerGeneralDocument customerGeneralDocument) {
        return new RestResponseDto()
                .successModel(customerGeneralDocumentService.save(customerGeneralDocument));
    }

    @PostMapping("/upload-document")
    public ResponseEntity<?> uploadPhoto(
            @RequestParam("file") @FileFormatValid MultipartFile multipartFile,
            @RequestParam("customerName") String name,
            @RequestParam("documentName") String documentName,
            @RequestParam("documentId") String documentId,
            @RequestParam("customerInfoId") String id,
            @RequestParam("customerType") String customerType
    ) {
        Long customerInfoId = Long.valueOf(id);
        Long docId = Long.valueOf(documentId);
        CustomerGeneralDocument customerGeneralDocument = customerGeneralDocumentService.findByCustomerInfoIdAndDocumentId(
                customerInfoId, docId);
        if (customerGeneralDocument == null) {
            customerGeneralDocument = new CustomerGeneralDocument();
            Document document = documentService.findOne(docId);
            customerGeneralDocument.setDocument(document);
            customerGeneralDocument.setCustomerInfoId(customerInfoId);
        } else {
            int version = customerGeneralDocument.getVersion();
            customerGeneralDocument.setVersion(version + 1);
        }
        Long branchId = userService.getAuthenticatedUser().getBranch().get(0).getId();
        Preconditions.checkNotNull(name.equals("undefined") || name.equals("null") ? null
                : (StringUtils.isEmpty(name) ? null : name), "Customer Name "
                + "is required to upload file.");

        String basePath = new PathBuilder(UploadDir.initialDocument)
                .buildCustomerInfoBasePathWithId(customerInfoId, branchId, customerType);
        String uploadPath = new StringBuilder(basePath)
                .append("generalDoc")
                .append("/")
                .toString();
        logger.info("File Upload Path {}", uploadPath);
        ResponseEntity<?> responseEntity = FileUploadUtils
                .uploadFile(multipartFile, uploadPath, documentName);
        customerGeneralDocument
                .setDocPath(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        customerGeneralDocumentService.save(customerGeneralDocument);
        return new RestResponseDto().successModel(customerGeneralDocument);
    }

    @GetMapping(value = "/{customerInfoID}")
    public ResponseEntity save(@PathVariable Long customerInfoID) {
        return new RestResponseDto()
                .successModel(customerGeneralDocumentService.findByCustomerInfoId(customerInfoID));
    }

    @PostMapping("/delete-document/{id}/{customerInfoId}")
    public ResponseEntity deleteDocument(@RequestBody String path, @PathVariable Long id,
                                         @PathVariable Long customerInfoId) {
        return new RestResponseDto()
                .successModel(customerGeneralDocumentService.deleteByDocId(id, customerInfoId, path));
    }

    @PostMapping("/delete-document/{id}/{customerInfoId}/{docIndex}")
    public ResponseEntity deleteDocument(@RequestBody String path,
                                         @PathVariable Long id,
                                         @PathVariable Long customerInfoId,
                                         @PathVariable Integer docIndex) {
        return new RestResponseDto()
                .successModel(customerGeneralDocumentService.deleteByDocIndex(id, customerInfoId, path, docIndex));
    }

    @PostMapping("/upload-multiple-document")
    public ResponseEntity<?> uploadMultiplePhoto(@RequestParam("file") @MultipleFileFormatValid List<MultipartFile> multipartFile,
                                                 @RequestParam("documentId") String documentId,
                                                 @RequestParam("documentName") String documentName,
                                                 @RequestParam("customerName") String name,
                                                 @RequestParam("customerInfoId") String id,
                                                 @RequestParam("customerType") String customerType
    ) {
        ResponseEntity<?> responseEntity = null;
        List<String> savedFileName = new ArrayList<>();
        Long customerInfoId = Long.valueOf(id);
        Long docId = Long.valueOf(documentId);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.mm.dd.HH.mm.ss.SSS");
        String stringDate = formatter.format(new Date());
        Date date = null;
        try {
            date = (Date) formatter.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        int doc_index = 0;
        for (int i = 0; i < multipartFile.size(); i++) {
            long timeStamp = date.getTime();
            CustomerGeneralDocument oldCustomerGeneralDocument = customerGeneralDocumentService
                    .findFirstByCustomerInfoIdAndDocumentOrderByDocIndexDesc(customerInfoId, docId);
            if (oldCustomerGeneralDocument == null) {
                doc_index = 0;
            } else {
                doc_index = oldCustomerGeneralDocument.getDocIndex();
            }
            CustomerGeneralDocument customerGeneralDocument = new CustomerGeneralDocument();
            Document document = documentService.findOne(docId);
            customerGeneralDocument.setDocument(document);
            customerGeneralDocument.setCustomerInfoId(customerInfoId);
            customerGeneralDocument.setDocIndex(doc_index + 1);
            MultipartFile file = multipartFile.get(i);
            String fileName = documentName.concat("-").concat(String.valueOf(timeStamp)).concat("-").concat(String.valueOf(r.nextInt(9999)));
            Long branchId = userService.getAuthenticatedUser().getBranch().get(0).getId();
            Preconditions.checkNotNull(name.equals("undefined") || name.equals("null") ? null
                    : (StringUtils.isEmpty(name) ? null : name), "Customer Name "
                    + "is required to upload file.");
            String basePath = new PathBuilder(UploadDir.initialDocument)
                    .buildCustomerInfoBasePathWithId(customerInfoId, branchId, customerType);
            String uploadPath = new StringBuilder(basePath)
                    .append("generalDoc")
                    .append("/")
                    .toString();
            logger.info("File Upload Path {}", uploadPath);
            responseEntity = FileUploadUtils
                    .uploadFile(multipartFile.get(i), uploadPath, fileName);
            customerGeneralDocument
                    .setDocPath(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
            customerGeneralDocumentService.save(customerGeneralDocument);
            savedFileName.add(((RestResponseDto) responseEntity.getBody()).getDetail().toString());
        }
        return new RestResponseDto()
                .successModel(savedFileName);
    }
}
