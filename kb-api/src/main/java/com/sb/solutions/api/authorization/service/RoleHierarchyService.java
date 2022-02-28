package com.sb.solutions.api.authorization.service;

import java.util.List;

import com.sb.solutions.api.authorization.entity.RoleHierarchy;
import com.sb.solutions.core.service.BaseService;

/**
 * @author Rujan Maharjan on 5/13/2019
 */
public interface RoleHierarchyService extends BaseService<RoleHierarchy> {

    List<RoleHierarchy> saveList(List<RoleHierarchy> roleHierarchyList);

    List<RoleHierarchy> roleHierarchyByCurrentRoleForward(Long id);

    List<RoleHierarchy> roleHierarchyByCurrentRoleBackward(Long id);
}
