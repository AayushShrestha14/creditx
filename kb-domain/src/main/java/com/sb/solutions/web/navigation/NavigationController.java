package com.sb.solutions.web.navigation;

import static com.sb.solutions.core.constant.AppConstant.ELIGIBILITY_PERMISSION;
import static com.sb.solutions.core.constant.AppConstant.ELIGIBILITY_PERMISSION_SUBNAV_GENERAL_QUESTION;
import static com.sb.solutions.core.constant.AppConstant.ELIGIBILITY_PERMISSION_SUBNAV_QUESTION;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.ALL_CAD_FILE;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.CREDIT_ADMINISTRATION;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.DISBURSEMENT_APPROVED;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.DISBURSEMENT_PENDING;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.OFFER_LETTER_APPROVED;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.OFFER_LETTER_PENDING;
import static com.sb.solutions.web.navigation.mapper.MenuCreatorCAD.UNASSIGNED_APPROVED_LOAN;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchyService;
import com.sb.solutions.api.authorization.entity.RolePermissionRights;
import com.sb.solutions.api.authorization.service.RolePermissionRightService;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.AppConstant;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.utils.ApprovalType;
import com.sb.solutions.core.utils.FilterJsonUtils;
import com.sb.solutions.core.utils.ProductUtils;
import com.sb.solutions.web.navigation.dto.MenuDto;
import com.sb.solutions.web.navigation.mapper.MenuCreatorCAD;
import com.sb.solutions.web.navigation.mapper.MenuMapper;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
@RestController
@RequestMapping("/v1")
public class NavigationController {

    private final RolePermissionRightService rolePermissionRightService;

    private final ApprovalRoleHierarchyService approvalRoleHierarchyService;


    private final UserService userService;

    private final MenuMapper menuMapper;

    private static final String POST_APPROVAL_DOCUMENTATION = "Post Approval Documentation";
    private static final String POST_APPROVAL_DOCUMENTATION_LINK = "/home/loan/loan-offer-letter";
    private static final String POST_APPROVAL_DOCUMENTATION_ICON = "arrowhead-down-outline";

    public NavigationController(
        RolePermissionRightService rolePermissionRightService,
        ApprovalRoleHierarchyService approvalRoleHierarchyService,
        UserService userService,
        MenuMapper menuMapper) {
        this.rolePermissionRightService = rolePermissionRightService;
        this.approvalRoleHierarchyService = approvalRoleHierarchyService;
        this.userService = userService;
        this.menuMapper = menuMapper;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/nav")
    public ResponseEntity<?> getNav() {
        User u = userService.getAuthenticatedUser();
        return new RestResponseDto()
            .successModel(rolePermissionRightService.getByRoleId(u.getRole().getId()));
    }

    @GetMapping("/menu")
    public ResponseEntity<?> getMenu() {
        User u = userService.getAuthenticatedUser();
        List<RolePermissionRights> rolePermissionRights = rolePermissionRightService
            .getByRoleId(u.getRole().getId());
          /*
        Beside admin user, no other user can set question for eligibility
        below logic is to guard
         */
        setUpElibilityPermission(u, rolePermissionRights);

        List<MenuDto> menuList = menuMapper.menuDtoList(rolePermissionRights);
        if (ProductUtils.OFFER_LETTER) {
           boolean isPresentInCadHierarchy = approvalRoleHierarchyService
                    .checkRoleContainInHierarchies(u.getRole().getId(), ApprovalType.CAD, 0l);

            if ((isPresentInCadHierarchy || u.getRole().getRoleType().equals(RoleType.CAD_ADMIN))
                && (!ProductUtils.FULL_CAD) && !ProductUtils.CAD_LITE_VERSION) {
                MenuDto menuDto = new MenuDto();
                menuDto.setTitle(POST_APPROVAL_DOCUMENTATION);
                menuDto.setLink(POST_APPROVAL_DOCUMENTATION_LINK);
                menuDto.setIcon(POST_APPROVAL_DOCUMENTATION_ICON);
                menuList.add(menuDto);
            }

            List<MenuDto> menuDtos = menuList.stream()
                .filter(menuDto -> menuDto.getTitle().equalsIgnoreCase(CREDIT_ADMINISTRATION))
                .collect(
                    Collectors.toList());

            int size = menuDtos.size();
            if (size > 0) {
                menuList.remove(menuDtos.get(0));
            }
            if ((isPresentInCadHierarchy && ProductUtils.FULL_CAD)) {
                menuList.add(getMenuForCADFULL(u));
            } else if (ProductUtils.FULL_CAD && (
                u.getRole().getRoleType().equals(RoleType.CAD_SUPERVISOR)
                    || u.getRole().getRoleType()
                    .equals(RoleType.CAD_ADMIN) || u.getRole().getRoleType()
                    .equals(RoleType.ADMIN))) {
                menuList.add(getMenuForCADFULL(u));
            } else if ((!isPresentInCadHierarchy && ProductUtils.FULL_CAD)) {
                MenuDto menuDto = MenuCreatorCAD.cadNav();
                menuDto.setChildren(
                    menuDto.getChildren().stream().filter(f ->
                        (f.getTitle().equalsIgnoreCase(ALL_CAD_FILE)

                        ))
                        .collect(Collectors.toList()));
                menuList.add(menuDto);
            }
        }
        return new RestResponseDto().successModel(
            menuList.stream()
                    .filter(FilterJsonUtils.distinctByKey(MenuDto::getTitle))
                    .collect(
                Collectors.toList()));
    }

    private void setUpElibilityPermission(User u, List<RolePermissionRights> rolePermissionRights) {
        boolean hasEligibilityPermission = rolePermissionRights.stream()
            .anyMatch(r -> r.getPermission().getId() == ELIGIBILITY_PERMISSION);
        if (!u.getRole().getRoleName().equals(AppConstant.ADMIN_ROLE) && hasEligibilityPermission) {
            rolePermissionRights.stream().forEach(role -> {
                if (role.getPermission().getId() == ELIGIBILITY_PERMISSION) {
                    role.getPermission().getSubNavs()
                        .removeIf(subNav -> subNav.getId() == ELIGIBILITY_PERMISSION_SUBNAV_QUESTION
                            || subNav.getId() == ELIGIBILITY_PERMISSION_SUBNAV_GENERAL_QUESTION);
                }
            });

        }
    }


    private MenuDto getMenuForCADFULL(User u) {
        MenuDto menuDto = MenuCreatorCAD.cadNav();
        if (!(u.getRole().getRoleType().equals(RoleType.CAD_SUPERVISOR) || u.getRole().getRoleType()
            .equals(RoleType.CAD_ADMIN))) {
            menuDto.setChildren(
                menuDto.getChildren().stream()
                    .filter(f -> !(f.getTitle().equalsIgnoreCase(UNASSIGNED_APPROVED_LOAN)))
                    .collect(Collectors.toList()));
        }
        if (u.getRole().getRoleType().equals(RoleType.CAD_LEGAL)) {
            menuDto.setChildren(
                menuDto.getChildren().stream().filter(f ->
                    !(f.getTitle().equalsIgnoreCase(UNASSIGNED_APPROVED_LOAN)
                        || f.getTitle().equalsIgnoreCase(OFFER_LETTER_PENDING)
                        || f.getTitle().equalsIgnoreCase(OFFER_LETTER_APPROVED)
                        || f.getTitle().equalsIgnoreCase(DISBURSEMENT_PENDING)
                        || f.getTitle().equalsIgnoreCase(DISBURSEMENT_APPROVED)
                    ))
                    .collect(Collectors.toList()));
        }
        if (u.getRole().getRoleType().equals(RoleType.ADMIN)) {
            menuDto.setChildren(
                menuDto.getChildren().stream().filter(f ->
                    (f.getTitle().equalsIgnoreCase(ALL_CAD_FILE)
                    ))
                    .collect(Collectors.toList()));
        }
        return menuDto;
    }

}
