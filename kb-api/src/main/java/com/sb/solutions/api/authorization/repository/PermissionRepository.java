package com.sb.solutions.api.authorization.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.authorization.entity.Permission;

/**
 * Created by Rujan Maharjan on 3/25/2019.
 */

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "select p from Permission p where p.id not in (2,3,6,142) ")
    List<Permission> getAllForRoleAndPermission();

    @Query(value = "select ua.type,p.permission_name from url_api ua"
        + " left join role_permission_rights_api_rights apirights"
        + " on apirights.api_rights_id = ua.id"
        + " left join role_permission_rights rpr on rpr.id= apirights.role_permission_rights_id"
        + " left join role r on rpr.role_id = r.id"
        + " left join permission p on p.id = rpr.permission_id"
        + " where r.id=:role",
        nativeQuery = true)
    List<Map<String, Object>> permsRight(@Param("role") Long role);

    Permission  findPermissionByPermissionName(String name);
}
