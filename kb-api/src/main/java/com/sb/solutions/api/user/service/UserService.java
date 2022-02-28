package com.sb.solutions.api.user.service;

import com.sb.solutions.api.authorization.dto.RoleDto;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Sunil Babu Shrestha on 12/31/2018
 */


public interface UserService extends BaseService<User>, UserDetailsService {

    User getAuthenticatedUser();

    User getByUsername(String username);

    User save(User user);

    Page<User> findByRole(Collection<Role> roles, Pageable pageable);

    Map<Object, Object> userStatusCount();

    List<User> findByRoleId(Long id);

    List<User> findByRoleAndBranch(Long roleId, List<Long> branchIds);

    List<User> findByRoleIdAndIsDefaultCommittee(Long roleId, Boolean isTrue);

    List<User> findByRoleAndBranchId(Long roleId, Long branchId);

    List<User> findByRoleIdAndBranch(Long role, List<Long> branch);

    String csv(Object searchDto);

    List<Long> getRoleAccessFilterByBranch();

    String dismissAllBranchAndRole(User user);

    User updatePassword(String username, String password);

    List<RoleDto> getRoleWiseBranchWiseUserList(Long roleId, Long branchId, Long userId);

    boolean checkIfValidOldPassword(User user, String password);

    List<UserDto>  getUserByRoleCad();

    List<User> findByRoleTypeAndBranchIdAndStatusActive(RoleType roleType, Long branchId);

    List<UserDto> findByRoleIdAndBranchIdForDocumentAction(Long roleId, Long branchId);

    List<RoleDto> findByRoleInAndStatus(List<Role> roleList, Status status);

    List<UserDto> findUserListForSolByRoleIdInAndBranchId(List<Long> roleIds,Long branchId);

    List<User> getAllUserByCurrentRoleBranchAccess();

    String logout();

    String updateSecondaryRole(List<Long> roleList,Long id);

    @Transactional
   String switchUserRole(Role role);

    List<User>  getSecondaryUserByPrimaryUserID(Long id);

    void logoutAllUserByRole(Long roleId);


}
