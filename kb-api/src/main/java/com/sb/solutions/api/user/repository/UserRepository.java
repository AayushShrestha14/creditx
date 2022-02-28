package com.sb.solutions.api.user.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.RoleAccess;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 12/31/2018
 */
public interface UserRepository extends JpaRepository<User, Long>,
    JpaSpecificationExecutor<User> {

    User getUsersByUsername(String username);

    User getUsersByUsernameAndStatus(String username, Status status);

    @Query(value = "SELECT * FROM users u JOIN users_branch ub ON ub.user_id=u.id"
        + " WHERE u.role_id=:role AND ub.branch_id IN (:branch)", nativeQuery = true)
    List<User> findByRoleIdAndBranch(@Param("role") Long role, @Param("branch") List<Long> branch);

    List<User> findByRoleRoleAccessAndRoleNotAndRoleId(RoleAccess roleAccess, Role role, Long id);

    List<User> findByRoleId(Long roleId);

    @Query("SELECT u FROM User u where u.role is not null and u.status = :status")
    List<User> findUserNotDisMissAndActive(@Param("status") Status status);


    Page<User> findByRoleIn(Collection<Role> roles, Pageable pageable);

    @Query(value = "select "
        + "  (select  count(id) FROM users WHERE status=1) active,"
        + " (select  count(id) FROM users WHERE status=0) inactive,"
        + " (select  count(id) FROM users) users\n", nativeQuery = true)
    Map<Object, Object> userStatusCount();


    List<User> findByRoleIdAndBranchId(Long roleId, Long branchId);

    @Query(value =
        "  SELECT a.type FROM role r "
            + " LEFT JOIN role_permission_rights rpr ON rpr.role_id = r.id"
            + " LEFT JOIN role_permission_rights_api_rights rprar"
            + " ON rprar.role_permission_rights_id=rpr.id"
            + " LEFT JOIN url_api a ON rprar.api_rights_id = a.id "
            + " WHERE "
            + " r.id = :id\n"
            + " AND a.type is not null;", nativeQuery = true)
    List<Object> userApiAuthorities(@Param("id") Long id);

    List<User> findByRoleIdAndIsDefaultCommittee(Long id, Boolean isCommittee);


    List<User> findByRoleRoleNameAndStatus(String roleName, Status status);

    List<User> findByRoleRoleTypeAndBranchIdAndStatus(RoleType roleType, Long branchId,
        Status status);

    List<User> findByRoleIdAndBranchInAndStatus(Long roleId, List<Branch> branchId, Status status);

    List<User> findByRoleInAndStatus(List<Role> roleList, Status status);

    List<User> findByRoleIdInAndStatus(List<Long> roleList, Status status);

    List<User> findAllByBranchIn(List<Branch> branches);

    User findByPrimaryUserIdAndRoleId(Long pId,Long rId);

    List<User> findAllByPrimaryUserId(Long id);




}

