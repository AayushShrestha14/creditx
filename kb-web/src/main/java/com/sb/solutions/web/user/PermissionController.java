package com.sb.solutions.web.user;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.authorization.entity.Permission;
import com.sb.solutions.api.authorization.service.PermissionService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
@RestController
@RequestMapping("/v1/permission")
public class PermissionController {

    private final PermissionService permissionService;

    private final UserService userService;

    public PermissionController(
        @Autowired PermissionService permissionService,
        @Autowired UserService userService) {

        this.permissionService = permissionService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> savePermission(@Valid @RequestBody Permission permission) {

        final Permission r = permissionService.save(permission);

        if (r == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            return new RestResponseDto().successModel(r.getPermissionName());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getPermission() {
        return new RestResponseDto().successModel(permissionService.findAll().stream().filter(
            permission -> !permission.getPermissionName()
                .equalsIgnoreCase("Credit Administration")));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/chkPerm")
    public ResponseEntity<?> getPermChk() {
        User u = userService.getAuthenticatedUser();
        return new RestResponseDto()
            .successModel(permissionService.permsRight(u.getRole().getId()));
    }

}
