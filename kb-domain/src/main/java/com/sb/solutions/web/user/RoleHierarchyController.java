package com.sb.solutions.web.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.authorization.entity.RoleHierarchy;
import com.sb.solutions.api.authorization.service.RoleHierarchyService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.web.user.dto.RoleHierarchyDto;
import com.sb.solutions.web.user.mapper.RoleHierarchyMapper;

/**
 * @author Rujan Maharjan on 5/13/2019
 */

@RestController
@RequestMapping("/v1/role-hierarchy")
public class RoleHierarchyController {

    private final Logger logger = LoggerFactory.getLogger(RoleHierarchyController.class);

    private final RoleHierarchyMapper roleHierarchyMapper;

    private final RoleHierarchyService roleHierarchyService;

    private final UserService userService;

    public RoleHierarchyController(
        @Autowired RoleHierarchyMapper roleHierarchyMapper,
        @Autowired RoleHierarchyService roleHierarchyService,
        @Autowired UserService userService) {
        this.roleHierarchyMapper = roleHierarchyMapper;
        this.roleHierarchyService = roleHierarchyService;
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<?> saveRoleHierarchyList(
        @RequestBody List<RoleHierarchyDto> roleHierarchyDtoList) {
        final List<RoleHierarchy> roleHierarchies = roleHierarchyService
            .saveList(roleHierarchyMapper.mapDtosToEntities(roleHierarchyDtoList));

        if (roleHierarchies.isEmpty()) {
            logger.error("Error while saving Role_Hierarchy {}", roleHierarchyDtoList);
            return new RestResponseDto()
                .failureModel("Error occurred while saving Role_Hierarchy " + roleHierarchyDtoList);
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
