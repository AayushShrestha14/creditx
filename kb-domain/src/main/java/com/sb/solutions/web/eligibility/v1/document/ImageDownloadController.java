package com.sb.solutions.web.eligibility.v1.document;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.filestorage.service.FileStorageService;
import com.sb.solutions.core.dto.RestResponseDto;

@RestController
public class ImageDownloadController {

    private final Logger logger = LoggerFactory.getLogger(ImageDownloadController.class);

    private final FileStorageService fileStorageService;

    public ImageDownloadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping(path = "/applicant-documents/{encodedHash}/{fileName:.+}")
    public final ResponseEntity<?> getImage(
        @PathVariable String encodedHash, @PathVariable String fileName,
        HttpServletRequest request) {

        logger.debug("Getting image.");
        final Resource resource = fileStorageService.getFile(encodedHash, fileName);
        if (resource == null) {
            return new RestResponseDto().failureModel("Unable to access resource");
        }
        try {
            final String contentType = request.getServletContext()
                .getMimeType(resource.getFile().getAbsolutePath());

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);

        } catch (IOException e) {
            return new RestResponseDto().failureModel("Something went wrong.");
        }
    }
}
