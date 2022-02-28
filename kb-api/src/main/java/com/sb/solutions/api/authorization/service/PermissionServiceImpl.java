package com.sb.solutions.api.authorization.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.authorization.entity.Permission;
import com.sb.solutions.api.authorization.entity.UrlApi;
import com.sb.solutions.api.authorization.repository.PermissionRepository;
import com.sb.solutions.api.authorization.repository.RolePermissionRightRepository;

/**
 * @author Rujan Maharjan on 3/28/2019
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    RolePermissionRightRepository rolePermissionRightRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.getAllForRoleAndPermission();
    }

    @Override
    public Permission findOne(Long id) {
        return permissionRepository.findById(id).get();
    }

    @Override
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    public Page<Permission> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Permission> saveAll(List<Permission> list) {
        return permissionRepository.saveAll(list);
    }

    @Override
    public List<UrlApi> permsRight(Long role) {
        return rolePermissionRightRepository.permsRight(role);
    }

    @Override
    public Permission findByName(String name) {
        return permissionRepository.findPermissionByPermissionName(name.trim());
    }
}
