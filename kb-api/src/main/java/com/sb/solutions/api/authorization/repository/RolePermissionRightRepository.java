package com.sb.solutions.api.authorization.repository;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.authorization.entity.RolePermissionRights;
import com.sb.solutions.api.authorization.entity.UrlApi;

/**
 * Created by Rujan Maharjan on 3/25/2019.
 */

@Repository
public interface RolePermissionRightRepository extends JpaRepository<RolePermissionRights, Long> {

    @Query("Select p from RolePermissionRights p"
        + " where p.role.id=:id and p.role.status=1 AND p.permission.status=1"
        + " order by p.permission.orders ASC ")
    List<RolePermissionRights> findByRole(@Param("id") Long id);

    @Query("Select p from RolePermissionRights p"
            + " where p.role.id=:id and p.role.status=1 AND p.permission.status=1 AND p.permission.visible = true"
            + " order by p.permission.orders ASC ")
    List<RolePermissionRights> findByRoleVisible(Long id);


    @Transactional
    @Modifying
    @Query(value = "delete from role_permission_rights where role_id=:id and permission_id=:pid",
        nativeQuery = true)
    void deleteRolePermissionRightsByRole(@Param("id") Long i, @Param("pid") Long pid);


    @Query("SELECT r.apiRights FROM RolePermissionRights r where r.role.id =:role ")
    List<UrlApi> permsRight(@Param("role") Long role);

}
