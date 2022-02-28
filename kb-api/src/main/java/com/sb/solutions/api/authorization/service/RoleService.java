package com.sb.solutions.api.authorization.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
public interface RoleService extends BaseService<Role> {

    Map<Object, Object> roleStatusCount();

    List<Map<Object, Object>> activeRole();

    boolean isMaker();

    Role getRoleCAD();

    List<Role> getByRoleTypeAndStatus(RoleType roleType, Status status);
}

