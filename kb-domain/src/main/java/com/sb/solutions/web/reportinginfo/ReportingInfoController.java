package com.sb.solutions.web.reportinginfo;

import java.util.Map;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.reportinginfo.entity.ReportingInfo;
import com.sb.solutions.api.reportinginfo.service.ReportingInfoService;
import com.sb.solutions.core.dto.RestResponseDto;

/**
 * @author Elvin Shrestha on 3/27/2020
 */
@RestController
@RequestMapping(ReportingInfoController.URL)
public class ReportingInfoController {

    static final String URL = "/v1/reporting-info";

    private static final Logger logger = LoggerFactory.getLogger(ReportingInfoController.class);

    private final ReportingInfoService service;

    public ReportingInfoController(
        ReportingInfoService service) {
        this.service = service;
    }

    @PostMapping("/initial")
    public ResponseEntity<?> initialSave(@Valid @RequestBody ReportingInfo reportingInfo) {
        if (reportingInfo.getId() != null) {
            ReportingInfo find = service.findOne(reportingInfo.getId()).orElse(null);
            if (find == null) {
                return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "Report not found");
            }
            // update name only
            find.setName(reportingInfo.getName());
            return new RestResponseDto().successModel(service.save(find));
        }
        return new RestResponseDto().successModel(service.save(reportingInfo));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ReportingInfo reportingInfo) {
        return new RestResponseDto().successModel(service.save(reportingInfo));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(service.findAll());
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllBySpec(@RequestBody Object search) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = objectMapper.convertValue(search, Map.class);
        return new RestResponseDto().successModel(service.findAllBySpec(params));
    }

}
