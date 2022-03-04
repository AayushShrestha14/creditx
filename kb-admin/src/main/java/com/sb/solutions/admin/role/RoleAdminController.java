package com.sb.solutions.admin.role;

import com.sb.solutions.api.authorization.entity.Permission;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.entity.RoleHierarchy;
import com.sb.solutions.api.authorization.entity.RolePermissionRights;
import com.sb.solutions.api.authorization.service.RoleHierarchyService;
import com.sb.solutions.api.authorization.service.RolePermissionRightService;
import com.sb.solutions.api.authorization.service.RoleService;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rujan Maharjan on 3/28/2019
 */

@RestController
@RequestMapping("/v1/admin/role")
public class RoleAdminController {

    private final RoleService roleService;

    private final RoleHierarchyService roleHierarchyService;

    private final RolePermissionRightService rolePermissionRightService;

    public RoleAdminController(
            @Autowired RoleService roleService,
            @Autowired RoleHierarchyService roleHierarchyService, RolePermissionRightService rolePermissionRightService) {
        this.roleService = roleService;
        this.roleHierarchyService = roleHierarchyService;
        this.rolePermissionRightService = rolePermissionRightService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveRole(@Valid @RequestBody Role role) {
        final Role r = roleService.save(role);

        if (r == null) {
            return new RestResponseDto().failureModel("Error Occurs");
        } else {
            if (role.getId() != null && !(role.getRoleType().equals(RoleType.CAD_ADMIN) || role.getRoleType().equals(RoleType.ADMIN)
                    || role.getRoleType().equals(RoleType.CAD_SUPERVISOR) || role.getRoleType().equals(RoleType.CAD_LEGAL))) {
                List<RoleHierarchy> roleHierarchies = roleHierarchyService.findAll();
                RoleHierarchy roleHierarchy = new RoleHierarchy();
                roleHierarchy.setRole(r);
                roleHierarchy.setRoleOrder(roleHierarchies.size() + 1L);
                roleHierarchyService.save(roleHierarchy);
            }
            Permission permission = new Permission();
            permission.setId(17L);
            List<RolePermissionRights> rolePermissionRightsList = new ArrayList<>();
            RolePermissionRights rolePermissionRights = new RolePermissionRights();
            rolePermissionRights.setRole(r);
            rolePermissionRights.setPermission(permission);
            rolePermissionRights.setApiRights(new ArrayList<>());
            rolePermissionRightsList.add(rolePermissionRights);
            permission = new Permission();
            permission.setId(102L);
            rolePermissionRights = new RolePermissionRights();
            rolePermissionRights.setRole(r);
            rolePermissionRights.setPermission(permission);
            rolePermissionRights.setApiRights(new ArrayList<>());
            rolePermissionRightsList.add(rolePermissionRights);
            rolePermissionRightService.saveList(rolePermissionRightsList);
            return new RestResponseDto().successModel(r.getRoleName());
        }
    }

    @GetMapping("/all")
    @Produces("application/json")
    public ResponseEntity<?> getRole() {
        return new RestResponseDto().successModel(roleService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/statusCount")
    public ResponseEntity<?> getRoleStatusCount() {
        return new RestResponseDto().successModel(roleService.roleStatusCount());
    }

    @RequestMapping(method = RequestMethod.POST, value = "/edit")

    public ResponseEntity<?> editRole(@Valid @RequestBody Role role) {
        final Role r = roleService.save(role);

        return new RestResponseDto().successModel(r.getRoleName());

    }

    @GetMapping("/{id}")
    @Produces("application/json")
    public ResponseEntity<?> getByRoleId(@PathVariable("id") Long id) {
        return new RestResponseDto().successModel(roleService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/active")
    public ResponseEntity<?> getActiveRole() {
        return new RestResponseDto().successModel(roleService.activeRole());
    }

    @GetMapping("maker")
    public ResponseEntity<?> chkMaker() {
        return new RestResponseDto().successModel(roleService.isMaker());
    }

    @GetMapping("getApproval")
    public ResponseEntity<?> getApproval() {
        return new RestResponseDto().successModel(roleService.getByRoleTypeAndStatus(RoleType.APPROVAL, Status.ACTIVE));
    }

}
