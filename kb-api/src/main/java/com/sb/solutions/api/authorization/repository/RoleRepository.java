package com.sb.solutions.api.authorization.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.authorization.dto.RoleDto;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;

/**
 * Created by Rujan Maharjan on 3/25/2019.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select "
        + "  (select  count(id) from role where status=1) active, "
        + " (select  count(id) from role where status=0) inactive, "
        + " (select  count(id) from role) roles ", nativeQuery = true)
    Map<Object, Object> roleStatusCount();

    @Query(value = "Select distinct p.id as id,p.role_name as roleName from role p "
        + "where p.status=1 and p.id <> 1 ", nativeQuery = true)
    List<Map<Object, Object>> activeRole();

    @Query("select"
        + " new com.sb.solutions.api.authorization.entity.Role(r.id,r.roleName,r.status,"
        + " (SELECT u.username from User u where r.createdBy=u.id),"
        + " (SELECT u.username from User u where r.modifiedBy=u.id),"
        + " r.roleType,r.roleAccess,r.authorityLabel,r.version) from Role r")
    List<Role> findAll();

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Role c"
        + " WHERE c.roleType = :roleType")
    boolean chkByRoleType(@Param("roleType") RoleType roleType);

    List<Role> getAllByRoleTypeEqualsAndStatus(RoleType roleType, Status status);

    @Query("select new com.sb.solutions.api.authorization.dto.RoleDto(r.id,r.roleName)  from Role r")
    List<RoleDto> getRoleDto();

    @Query("SELECT r FROM Role r WHERE r.roleName = 'CAD'")
    Role getRoleCAD();

    List<Role> findAllByIdInAndStatus(List<Long> roleIds,Status status);
}
