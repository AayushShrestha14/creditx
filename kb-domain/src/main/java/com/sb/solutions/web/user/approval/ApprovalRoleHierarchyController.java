package com.sb.solutions.web.user.approval;

import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchy;
import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchyService;
import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchyServiceImpl;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.utils.ApprovalType;

@RestController
@RequestMapping("/v1/approval/role-hierarchies")
public class ApprovalRoleHierarchyController {

    private static final Logger logger = LoggerFactory
        .getLogger(ApprovalRoleHierarchyController.class);

    private final ApprovalRoleHierarchyService service;
    private final ApprovalRoleHierarchyMapper mapper;

    public ApprovalRoleHierarchyController(
        ApprovalRoleHierarchyServiceImpl service,
        ApprovalRoleHierarchyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> save(
        @RequestBody @Valid List<ApprovalRoleHierarchyDto> hierarchies) {

        final List<ApprovalRoleHierarchy> roleHierarchies = mapper.mapDtosToEntities(hierarchies);

        if (roleHierarchies.isEmpty()) {
            logger.error("Approval Role Hierarchey can not be empty");
            return new RestResponseDto()
                .failureModel("Approval Role Hierarchey can not be empty");
        }

        final List<ApprovalRoleHierarchy> savedHierarchies = service
            .saveAll(roleHierarchies);

        final List<ApprovalRoleHierarchyDto> dtos = mapper.mapEntitiesToDtos(savedHierarchies);
        dtos.sort(Comparator.comparingLong(ApprovalRoleHierarchyDto::getRefId));

        return new RestResponseDto()
            .successModel(dtos);
    }

    @GetMapping("/default/{type}/{refId}")
    public ResponseEntity<?> getDefaultRoles(@PathVariable("type") ApprovalType type,
        @PathVariable("refId") long refId) {
        return new RestResponseDto()
            .successModel(mapper.mapEntitiesToDtos(service.getDefaultRoleHierarchies(type, refId)));
    }

    @GetMapping("/{type}/{refId}")
    public ResponseEntity<?> getRoles(@PathVariable("type") ApprovalType type,
        @PathVariable("refId") long refId) {

        final List<ApprovalRoleHierarchyDto> dtos = mapper
            .mapEntitiesToDtos(service.getRoles(type, refId));
        dtos.sort(Comparator.comparingLong(ApprovalRoleHierarchyDto::getRoleOrder));

        return new RestResponseDto()
            .successModel(dtos);
    }

    @GetMapping("/forward-roles/{roleId}/{type}/{refId}")
    public ResponseEntity<?> getForwardRolesForRoleWithType(
        @PathVariable("roleId") long roleId,
        @PathVariable("type") ApprovalType type,
        @PathVariable("refId") long refId) {

        final List<ApprovalRoleHierarchyDto> dtos = mapper.mapEntitiesToDtos(service
            .getForwardRolesForRoleWithType(roleId, type, refId));

        return new RestResponseDto().successModel(dtos);
    }

    @GetMapping("/forward-roles/{roleId}/{type}/{typeId}/{ref}/{refId}")
    public ResponseEntity<?> getForwardRolesForRole(
        @PathVariable("roleId") long roleId,
        @PathVariable("type") ApprovalType type,
        @PathVariable("typeId") long typeId,
        @PathVariable("ref") ApprovalType ref,
        @PathVariable("refId") long refId) {
        final List<ApprovalRoleHierarchyDto> dtos = mapper.mapEntitiesToDtos(service
            .getForwardRolesForRole(roleId, type, typeId, ref, refId));

        return new RestResponseDto().successModel(dtos);
    }

    @GetMapping("/backward-roles/{roleId}/{type}/{typeId}/{ref}/{refId}")
    public ResponseEntity<?> getBackwardRolesForRole(
        @PathVariable("roleId") Long roleId,
        @PathVariable("type") ApprovalType type,
        @PathVariable("typeId") long typeId,
        @PathVariable("ref") ApprovalType ref,
        @PathVariable("refId") long refId) {
        return new RestResponseDto()
            .successModel(
                mapper.mapEntitiesToDtos(
                    service.getBackwardRolesForRole(roleId, type, typeId, ref, refId)));
    }

    @GetMapping("/backward-roles/{roleId}/{type}/{refId}")
    public ResponseEntity<?> getBackwardRolesForRoleWithType(
        @PathVariable("roleId") Long roleId,
        @PathVariable("type") ApprovalType type,
        @PathVariable("refId") long refId) {
        return new RestResponseDto()
            .successModel(
                mapper.mapEntitiesToDtos(
                    service.getBackwardRolesForRoleWithType(roleId, type, refId)));
    }
}
