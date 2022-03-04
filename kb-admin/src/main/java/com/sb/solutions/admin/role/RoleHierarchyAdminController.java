package com.sb.solutions.admin.role;

import com.sb.solutions.admin.role.dto.RoleHierarchyAdminDto;
import com.sb.solutions.admin.role.mapper.RoleHierarchyAdminMapper;
import com.sb.solutions.api.authorization.entity.RoleHierarchy;
import com.sb.solutions.api.authorization.service.RoleHierarchyService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Rujan Maharjan on 5/13/2019
 */

@RestController
@RequestMapping("/v1/admin/role-hierarchy")
public class RoleHierarchyAdminController {

    private final Logger logger = LoggerFactory.getLogger(RoleHierarchyAdminController.class);

    private final RoleHierarchyAdminMapper roleHierarchyMapper;

    private final RoleHierarchyService roleHierarchyService;

    private final UserService userService;

    public RoleHierarchyAdminController(
        @Autowired RoleHierarchyAdminMapper roleHierarchyMapper,
        @Autowired RoleHierarchyService roleHierarchyService,
        @Autowired UserService userService) {
        this.roleHierarchyMapper = roleHierarchyMapper;
        this.roleHierarchyService = roleHierarchyService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<?> saveRoleHierarchyList(
        @RequestBody List<RoleHierarchyAdminDto> roleHierarchyAdminDtoList) {
        final List<RoleHierarchy> roleHierarchies = roleHierarchyService
            .saveList(roleHierarchyMapper.mapDtosToEntities(roleHierarchyAdminDtoList));

        if (roleHierarchies.isEmpty()) {
            logger.error("Error while saving Role_Hierarchy {}", roleHierarchyAdminDtoList);
            return new RestResponseDto()
                .failureModel("Error occurred while saving Role_Hierarchy " + roleHierarchyAdminDtoList);
        }
        return new RestResponseDto()
            .successModel(roleHierarchyMapper.mapEntitiesToDtos(roleHierarchies));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getRoleHierarchyList() {
        return new RestResponseDto()
            .successModel(roleHierarchyMapper.mapEntitiesToDtos(roleHierarchyService.findAll()));
    }

    @GetMapping("/getForward")
    public ResponseEntity<?> getRoleHierarchyListPerRoleForward() {
        User u = userService.getAuthenticatedUser();
        return new RestResponseDto().successModel(
            roleHierarchyService.roleHierarchyByCurrentRoleForward(u.getRole().getId()));
    }

    @GetMapping("/getBackward")
    public ResponseEntity<?> getRoleHierarchyListPerRoleBackward() {
        User u = userService.getAuthenticatedUser();
        return new RestResponseDto().successModel(
            roleHierarchyService.roleHierarchyByCurrentRoleBackward(u.getRole().getId()));
    }
}
