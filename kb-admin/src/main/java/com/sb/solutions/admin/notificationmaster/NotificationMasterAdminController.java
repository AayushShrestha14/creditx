package com.sb.solutions.admin.notificationmaster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.preference.notificationMaster.entity.NotificationMaster;
import com.sb.solutions.api.preference.notificationMaster.service.NotificationMasterService;
import com.sb.solutions.core.dto.RestResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/notification-master")
public class NotificationMasterAdminController {

    private final NotificationMasterService service;

    public NotificationMasterAdminController(
        @Autowired NotificationMasterService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody List<NotificationMaster> notificationMasters) {
        return new RestResponseDto().successModel(service.saveAll(notificationMasters));
    }

    @PostMapping("/one")
    public ResponseEntity<?> getOneWithSearch(@RequestBody Object search) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.convertValue(search, Map.class);
        return new RestResponseDto().successModel(service.findOneBySpec(map));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new RestResponseDto().successModel(service.findAll());
    }

    @PostMapping("/status")
    public ResponseEntity<?> updateStatus(@RequestBody NotificationMasterAdminDto dto) {
        NotificationMaster master = service.findOne(dto.getId()).orElse(null);
        if (master == null) {
            return new RestResponseDto().failureModel(HttpStatus.NOT_FOUND, "Not found!!!");
        }
        master.setStatus(dto.getStatus());
        return new RestResponseDto().successModel(service.save(master));
    }

}
