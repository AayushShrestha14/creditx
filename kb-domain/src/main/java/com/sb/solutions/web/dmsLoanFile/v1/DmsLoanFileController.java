package com.sb.solutions.web.dmsLoanFile.v1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.dms.dmsloanfile.entity.DmsLoanFile;
import com.sb.solutions.api.dms.dmsloanfile.repository.DmsLoanFileRepository;
import com.sb.solutions.api.dms.dmsloanfile.service.DmsLoanFileService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.PaginationUtils;

@RestController
@RequestMapping(value = "/v1/dms-loan-file")
@AllArgsConstructor
public class DmsLoanFileController {

    private static final Logger logger = LoggerFactory.getLogger(DmsLoanFileController.class);

    private final UserService userService;
    private final DmsLoanFileService dmsLoanFileService;
    private final DmsLoanFileRepository dmsLoanFileRepository;


    @PostMapping
    public ResponseEntity<?> saveLoanFile(@Valid @RequestBody DmsLoanFile dmsLoanFile) {
        final DmsLoanFile info = dmsLoanFileService.save(dmsLoanFile);

        if (info == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(info);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDmsLoanFileById(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(dmsLoanFileService.findOne(id));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getLoanFiles() {
        return new RestResponseDto().successModel(dmsLoanFileService.findAll());
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @RequestMapping(method = RequestMethod.POST, path = "/list")
    public ResponseEntity<?> getPageableLoanFile(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto().successModel(
            dmsLoanFileService.findAllPageable(searchDto, PaginationUtils.pageable(page, size)));
    }

//    @PostMapping("/uploadFile")
//    public ResponseEntity<?> uploadLoanFile(@RequestParam("file") MultipartFile multipartFile,
//        @RequestParam("type") String type,
//        @RequestParam("citizenNumber") String citizenNumber,
//        @RequestParam("customerName") String name,
//        @RequestParam("documentName") String documentName,
//        @RequestParam(name = "action", required = false, defaultValue = "new") String action) {
//
//        String branchName = userService.getAuthenticated().getBranch().get(0).getName()
//            .replace(" ", "_");
//        Preconditions.checkNotNull(citizenNumber.equals("null") ? null
//                : (StringUtils.isEmpty(citizenNumber) ? null : citizenNumber),
//            "Citizenship Number is required to upload file.");
//        Preconditions.checkNotNull(name.equals("undefined") || name.equals("null") ? null
//            : (StringUtils.isEmpty(name) ? null : name), "Customer Name "
//            + "is required to upload file.");
//        String uploadPath = new PathBuilder(UploadDir.initialDocument).withAction(action)
//            .isJsonPath(false).withBranch(branchName).withCitizenship(citizenNumber)
//            .withCustomerName(name).withLoanType(type).build();
//        logger.info("File Upload Path {}", uploadPath);
//        return FileUploadUtils
//            .uploadFile(multipartFile, uploadPath, documentName);
//
//    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestBody String path) {
        File file = new File(FilePath.getOSPath() + path);
        final InputStreamResource resource;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            logger.error("File not found {}", e.getMessage());
            return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "File not found ::" + e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok().headers(headers)
                .contentLength(file.length()).contentType(
                        MediaType.parseMediaType("application/txt")).<Object>body(resource);
    }


    @GetMapping("/statusCount")
    public ResponseEntity<?> getPendingStatusCount() {
        return new RestResponseDto().successModel(dmsLoanFileRepository.pendingStatusCount());
    }
}
