package com.sb.solutions.admin.role;

import com.sb.solutions.api.authorization.entity.RolePermissionRights;
import com.sb.solutions.api.authorization.service.RightService;
import com.sb.solutions.api.authorization.service.RolePermissionRightService;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.dto.RestResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Rujan Maharjan on 3/28/2019
 */

@RestController
@RequestMapping("/v1/admin/roleRightPermission")
public class RoleRightPermissionAdminController {

    private final Logger logger = LoggerFactory.getLogger(RoleRightPermissionAdminController.class);

    private final RolePermissionRightService rolePermissionRightService;

    private final RightService rightService;

    private final UserService userService;

    public RoleRightPermissionAdminController(
            @Autowired RolePermissionRightService rolePermissionRightService,
            @Autowired RightService rightService,
            UserService userService) {
        this.rolePermissionRightService = rolePermissionRightService;
        this.rightService = rightService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveRolePermRight(@Valid @RequestBody List<RolePermissionRights> rpr) {
        rolePermissionRightService.saveList(rpr);
        for(RolePermissionRights rights : rpr){
            userService.logoutAllUserByRole(rights.getRole().getId());
        }
        return new RestResponseDto().successModel(null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<?> getRolePermission(@PathVariable Long id) {

        return new RestResponseDto().successModel(rolePermissionRightService.getByRoleId(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/rights")
    public ResponseEntity<?> getRights() {

        return new RestResponseDto().successModel(rightService.getAll());
    }
}
