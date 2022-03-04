package com.sb.solutions.web.user;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.service.RoleService;
import com.sb.solutions.api.securityconfig.accesstoken.repository.AccessTokenRepository;
import com.sb.solutions.api.securityconfig.refreshtoken.repository.RefreshTokenRepository;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.constant.EmailConstant.Template;
import com.sb.solutions.core.dto.RestResponseDto;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.utils.PaginationUtils;
import com.sb.solutions.core.utils.date.DateManipulator;
import com.sb.solutions.core.utils.email.Email;
import com.sb.solutions.core.utils.email.MailSenderService;
import com.sb.solutions.core.utils.file.FileUploadUtils;
import com.sb.solutions.core.validation.constraint.FileFormatValid;
import com.sb.solutions.web.user.dto.ChangePasswordDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Sunil Babu Shrestha on 12/27/2018
 */
@RestController
@Validated
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final MailSenderService mailSenderService;

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${bank.name}")
    private String bankName;
    @Value("${bank.affiliateId}")
    private String affiliateId;

    @Value("${bank.frontaddress}")
    private String frontAddress;


    @Autowired
    public UserController(
            UserService userService,
            RoleService roleService,
            MailSenderService mailSenderService, AccessTokenRepository accessTokenRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.mailSenderService = mailSenderService;
        this.accessTokenRepository = accessTokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @GetMapping(path = "/authenticated")
    public ResponseEntity<?> getAuthenticated() {
        return new RestResponseDto().successModel(userService.getAuthenticatedUser());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getAuthenticated(@PathVariable Long id) {
        return new RestResponseDto().successModel(userService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        return new RestResponseDto().successModel(userService.save(user));
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> saveUserFile(@RequestParam("file") @FileFormatValid MultipartFile multipartFile,
        @RequestParam("type") String type) {
        return FileUploadUtils.uploadFile(multipartFile, type);
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "/list")
    public ResponseEntity<?> sergetAll(@RequestBody Object searchDto,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(userService.findAllPageable(searchDto, PaginationUtils
                .pageable(page, size)));
    }

    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
            value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
            value = "Number of records per page.")})
    @PostMapping(value = "listByRole")
    public ResponseEntity<?> getUserByRole(@RequestBody Collection<Role> roles,
        @RequestParam("page") int page, @RequestParam("size") int size) {
        return new RestResponseDto()
            .successModel(userService.findByRole(roles, PaginationUtils.pageable(page, size)));
    }

    @GetMapping(value = "listRole")
    public ResponseEntity<?> getRoleList() {
        return new RestResponseDto().successModel(roleService.findAll());
    }

    @GetMapping(value = "/{id}/users")
    public ResponseEntity<?> getUserList(@PathVariable Long id) {
        return new RestResponseDto().successModel(userService.findByRoleAndBranch(id, null));
    }


    @RequestMapping(method = RequestMethod.GET, path = "/statusCount")
    public ResponseEntity<?> getUserStatusCount() {
        return new RestResponseDto().successModel(userService.userStatusCount());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/csv")
    public ResponseEntity<?> csv(@RequestBody Object searchDto) {
        return new RestResponseDto().successModel(userService.csv(searchDto));
    }

    @PostMapping(value = "/dismiss")
    public ResponseEntity<?> dismissBranchAndRole(@RequestBody User user) {
        return new RestResponseDto().successModel(userService.dismissAllBranchAndRole(user));
    }

    @GetMapping(value = "/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("username") String username) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return new RestResponseDto().failureModel("User not found!");
        } else {
            String resetToken = UUID.randomUUID().toString() + new Date().getTime();
            user.setModifiedBy(user.getId());
            user.setResetPasswordToken(resetToken);

            DateManipulator dateManipulator = new DateManipulator(new Date());
            user.setResetPasswordTokenExpiry(dateManipulator.addDays(1));
            User savedUser = userService.save(user);

            // mailing
            Email email = new Email();
            email.setTo(savedUser.getEmail());
            email.setToName(savedUser.getName());
            email.setResetPasswordLink(
                    frontAddress + "#/newPassword?username=" + username + "&reset=" + resetToken);
            email.setExpiry(savedUser.getResetPasswordTokenExpiry().toString());
            email.setBankName(this.bankName);
            email.setAffiliateId(this.affiliateId);
            mailSenderService.send(Template.RESET_PASSWORD, email);

            return new RestResponseDto().successModel("SUCCESS");
        }
    }

    /**
     * PathVariable id represent currentUser Id
     * PathVariable  branchId represent Loan branch id
     */

    @GetMapping(path = "/get-all-doc-transfer/{id}/branch/{branchId}")
    public ResponseEntity<?> getAllForDocTransfer(@PathVariable Long id,
        @PathVariable Long branchId) {
        return new RestResponseDto()
            .successModel(userService.getRoleWiseBranchWiseUserList(null, branchId, id));
    }

    @PostMapping(value = "/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody User u) {
        User user = userService.getByUsername(u.getUsername());
        if (user == null) {
            return new RestResponseDto().failureModel("User not found!");
        } else {
            if (user.getResetPasswordToken() != null) {
                if (user.getResetPasswordTokenExpiry().before(new Date())) {
                    return new RestResponseDto()
                        .failureModel("Reset Token has been expired already");
                } else {
                    User updatedUser = userService.updatePassword(u.getUsername(), u.getPassword());
                    Email email = new Email();
                    email.setTo(updatedUser.getEmail());
                    email.setToName(updatedUser.getName());
                    email.setBankName(this.bankName);
                    email.setAffiliateId(this.affiliateId);
                    mailSenderService.send(Template.RESET_PASSWORD_SUCCESS, email);
                    if (ObjectUtils.isEmpty(updatedUser.getPrimaryUserId())) {
                        List<User> secondaryUser = userService
                            .getSecondaryUserByPrimaryUserID(updatedUser.getId());
                        secondaryUser.forEach(user1 -> {
                            userService.updatePassword(user1.getUsername(), u.getPassword());
                        });
                    }

                    accessTokenRepository.deleteAccessTokenByUserId(user.getId());
                    refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());

                    return new RestResponseDto()
                        .successModel(updatedUser);
                }
            } else {
                return new RestResponseDto().failureModel("Initiate Reset Password Process first");
            }
        }
    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto passwordDto) {
        User user = userService.getByUsername(passwordDto.getUsername());

        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            return new RestResponseDto().failureModel("Invalid Old Password");
        }
        userService.updatePassword(user.getUsername(), passwordDto.getNewPassword());
        if (ObjectUtils.isEmpty(user.getPrimaryUserId())) {
            List<User> secondaryUser = userService
                .getSecondaryUserByPrimaryUserID(user.getId());
            secondaryUser.forEach(user1 -> {
                userService.updatePassword(user1.getUsername(), passwordDto.getNewPassword());
            });
        }

        refreshTokenRepository.deleteRefreshTokenByUserId(user.getId());
        accessTokenRepository.deleteAccessTokenByUserId(user.getId());

        return new RestResponseDto().successModel("Password Changed Successfully");
    }

    @GetMapping(path = "/user-cad")
    public ResponseEntity<?> getUserCad() {

        return new RestResponseDto()
            .successModel(userService.getUserByRoleCad());
    }

    @GetMapping("/branch/all")
    public ResponseEntity<?> getAuthenticatedUserBranches() {
        return new RestResponseDto().successModel(
            userService.getRoleAccessFilterByBranch().stream().map(Object::toString).collect(
                Collectors.joining(",")));
    }

    @GetMapping(value = "/{id}/users/branch/{bId}")
    public ResponseEntity<?> getUserListForDocument(@PathVariable Long id, @PathVariable Long bId) {
        return new RestResponseDto()
            .successModel(userService.findByRoleIdAndBranchIdForDocumentAction(id, bId));
    }

    @PostMapping(value = "/role-list/branch/{bId}")
    public ResponseEntity<?> getUserListForSolByRoleIdInAndBranchId(@RequestBody List<Long> roleIds,
        @PathVariable Long bId) {
        return new RestResponseDto()
            .successModel(userService.findUserListForSolByRoleIdInAndBranchId(roleIds, bId));
    }

    @GetMapping(value = "/allUser")
    public ResponseEntity<?> allUser() {
        return new RestResponseDto()
            .successModel(userService.getAllUserByCurrentRoleBranchAccess());
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout() {
        return new RestResponseDto().successModel(userService.logout());
    }

    @PostMapping(value = "/update-roles/{id}")
    public ResponseEntity<?> logout(@PathVariable("id") Long id,
        @RequestBody List<Long> roleIDList) {
        return new RestResponseDto().successModel(userService.updateSecondaryRole(roleIDList, id));
    }

    @PostMapping(value = "/switch-user")
    public ResponseEntity<?> switchUser(@RequestBody Role role) {
        return new RestResponseDto().successModel(userService.switchUserRole(role));
    }

    @GetMapping(value = "/users/branch/{bId}/maker-active")
    public ResponseEntity<?> getUserListByBranchIdAndMakerActive(@PathVariable("bId") Long bId) {
        return new RestResponseDto()
            .successModel(userService.findByRoleTypeAndBranchIdAndStatusActive(RoleType.MAKER, bId));
    }

}
