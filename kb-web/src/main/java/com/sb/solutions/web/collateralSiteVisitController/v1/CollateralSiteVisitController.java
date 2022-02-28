package com.sb.solutions.web.collateralSiteVisitController.v1;

import com.sb.solutions.api.collateralSiteVisit.entity.CollateralSiteVisit;
import com.sb.solutions.api.collateralSiteVisit.entity.SiteVisitDocument;
import com.sb.solutions.api.collateralSiteVisit.service.CollateralSiteVisitService;
import com.sb.solutions.api.collateralSiteVisit.service.SiteVisitDocumentService;
import com.sb.solutions.core.constant.UploadDir;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PathBuilder;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sb.solutions.constant.SuccessMessage.DELETE_SUCCESS;

/**
 * Created by Mohammad Hussain on May, 2021
 */
@RestController
@RequestMapping(CollateralSiteVisitController.URL)
@Slf4j
public class CollateralSiteVisitController {
    static final String URL = "/v1/collateral-site-visit";
    static final String YES_FILE_EXIST = "Yes";
    static final String FILE_TYPE = ".jpg";
    private final CollateralSiteVisitService service;
    private final SiteVisitDocumentService siteVisitDocumentService;

    public CollateralSiteVisitController(CollateralSiteVisitService service, SiteVisitDocumentService siteVisitDocumentService) {
        this.service = service;
        this.siteVisitDocumentService = siteVisitDocumentService;
    }
    public final Logger logger = LoggerFactory.getLogger(CollateralSiteVisitController.class);

    @PostMapping("/{securityId}")
    public ResponseEntity<?> save(@PathVariable("securityId") Long securityId,
                                  @RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles,
                                  @RequestParam(value = "docName", required = false) List<String> docNames,
                                  @RequestParam(value = "isPrintable", required = false) List<String> isPrintable,
                                  @RequestParam("customerId") String customerId,
                                  @RequestParam("customerType") String customerType,
                                  @RequestParam("siteVisitData") String siteVisitDate,
                                  @RequestParam("securityName") String securityName,
                                  @RequestParam("siteVisitJsonData") String siteVisitJsonData,
                                  @RequestParam(value = "id", required = false) String id,
                                  @RequestParam(value = "docId", required = false) List<String> docId,
                                  @RequestParam(value = "fileExist", required = false) List<String> fileExist) {
        CollateralSiteVisit collateralSiteVisit = null;
        if (id != null) {
            collateralSiteVisit = service.findOne(Long.parseLong(id));
        }
        List<SiteVisitDocument> siteVisitDocuments = new ArrayList<>();
        if (collateralSiteVisit != null) {
            siteVisitDocuments = collateralSiteVisit.getSiteVisitDocuments();
        } else {
            collateralSiteVisit = new CollateralSiteVisit();
        }
        LocalDate localDate = LocalDate.parse(siteVisitDate);
        collateralSiteVisit.setSiteVisitDate(localDate);
        collateralSiteVisit.setSiteVisitJsonData(siteVisitJsonData);
        collateralSiteVisit.setSecurityName(securityName);
        CollateralSiteVisit collateralSiteVisit1 = service.saveCollateralSiteVisit(securityId, collateralSiteVisit);
        if (docNames != null && docNames.size()>0) {
            int index = 0;
            int fileIndex = 0;
            String basePath = new PathBuilder(UploadDir.initialDocument)
                    .buildCustomerSiteVisitPaths(Long.parseLong(customerId), customerType,
                            securityId, collateralSiteVisit1.getId());
            String uploadPath = new StringBuilder(basePath).toString();
            List<SiteVisitDocument> tempSiteVisitDocuments = new ArrayList<>();
            for (String docName: docNames) {
                SiteVisitDocument siteVisitDocument = new SiteVisitDocument();
                Long ids = Long.parseLong(docId.get(index));
                if (ids != -1) {
                    Optional<SiteVisitDocument> optionalSiteVisitDocument = siteVisitDocuments.stream().filter(f -> Objects.equals(f.getId(), ids)).findAny();
                    if (optionalSiteVisitDocument.isPresent()) {
                        siteVisitDocument = optionalSiteVisitDocument.get();
                    }
                }
                if (fileExist.get(index).equals(YES_FILE_EXIST)) {
                    MultipartFile file = multipartFiles.get(fileIndex);
                    if (!ObjectUtils.isEmpty(siteVisitDocument.getId())) {
                        // in case of edit if file is selected then replace the file matching old file name
                        FileUploadUtils.uploadFile(file, uploadPath, siteVisitDocument.getDocName().concat(FILE_TYPE));
                    } else {
                        // in case of new: create new file
                        FileUploadUtils.uploadFile(file, uploadPath, file.getOriginalFilename().concat(FILE_TYPE));
                    }
                    fileIndex++;
                }
                if (!ObjectUtils.isEmpty(siteVisitDocument.getId())) {
                    String oldDocName = siteVisitDocument.getDocName();
                    if (!oldDocName.equals(docName)) {
                        // in case if only name is updated: rename the file name
                        String oldPathAndDocName = siteVisitDocument.getDocPath() + oldDocName.concat(FILE_TYPE);
                        String newPathAndName = siteVisitDocument.getDocPath() + docName.concat(FILE_TYPE);
                        FileUploadUtils.moveFile(oldPathAndDocName, newPathAndName);
                    }
                }
                siteVisitDocument.setDocName(docName);
                siteVisitDocument.setDocPath(uploadPath);
                siteVisitDocument.setIsPrintable(isPrintable.get(index));
                siteVisitDocument.setSecurityName(securityName);
                // edit case update version and add document to list
                if (!ObjectUtils.isEmpty(siteVisitDocument.getId())) {
                    siteVisitDocument.setVersion(siteVisitDocument.getVersion()+1);
                    tempSiteVisitDocuments.add(siteVisitDocument);
                }else {
                    tempSiteVisitDocuments.add(siteVisitDocumentService.save(siteVisitDocument));
                }
                index++;
            }
            logger.info("site visit map file upload path {}", uploadPath);
            collateralSiteVisit.setSiteVisitDocuments(tempSiteVisitDocuments);
        }
        return new RestResponseDto().successModel(service.saveCollateralSiteVisit(securityId, collateralSiteVisit1));
    }

    @GetMapping("/{securityName}/{id}")
    public ResponseEntity<?> getCollateralSiteVisitBySecurityNameAndSecurityAndId(@PathVariable("securityName") String securityName, @PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.getCollateralSiteVisitBySecurityNameAndSecurityAndId(securityName, id));
    }

    @GetMapping("/site-visit/{siteVisitDate}/{id}")
    public ResponseEntity<?> getCollateralBySiteVisitDateAndId(@PathVariable("siteVisitDate")String siteVisitDate, @PathVariable("id") Long id) {
        LocalDate localDate = LocalDate.parse(siteVisitDate);
        return new RestResponseDto().successModel(service.getCollateralBySiteVisitDateAndId(localDate, id));
    }

    @GetMapping("/site-visit/{id}")
    public ResponseEntity<?> getCollateralSiteVisitBySecurityId(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(service.getCollateralSiteVisitBySecurityId(id));
    }

    @DeleteMapping("/delete-site-visit/{id}/{date}")
    public ResponseEntity<?> deleteSiteVisit(@PathVariable("id") Long id, @PathVariable String date) {
        LocalDate date1 = LocalDate.parse(date);
        CollateralSiteVisit collateralSiteVisit = service.getCollateralBySiteVisitDateAndId(date1, id);
        service.deleteSiteVisit(collateralSiteVisit);
        return new RestResponseDto().successModel(DELETE_SUCCESS);
    }

    @DeleteMapping("/delete-all-site-visit/{id}/{name}")
    public ResponseEntity<?> deleteAllSiteVisit(@PathVariable("id") Long id, @PathVariable String name) {
        service.deleteAllSiteVisit(id, name);
        return new RestResponseDto().successModel(DELETE_SUCCESS);
    }

}
