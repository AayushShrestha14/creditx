package com.sb.solutions.api.authorization.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.repository.RoleRepository;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.api.user.service.UserService;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 3/28/2019
 */

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserService userService;

    private final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public RoleServiceImpl(@Autowired RoleRepository roleRepository,
        @Autowired UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findOne(Long id) {
        return roleRepository.findById(id).get();
    }

    @Override
    public Role save(Role role) {
        logger.info("saving role {}", role);
        role.setRoleName(role.getRoleName().toUpperCase());
        final Role r = roleRepository.save(role);
        // check default user already exists or not
        User defaultCommunityUser = userService.getByUsername(r.getRoleName().concat("- default"));
        if (defaultCommunityUser == null) {

            if (role.getId() != null && role.getRoleType().equals(RoleType.COMMITTEE)) {
                User u = new User();
                u.setRole(r);

                u.setName(u.getRole().getRoleName().concat("- default"));
                u.setUsername(u.getRole().getRoleName().concat("- default"));
                u.setPassword(u.getRole().getRoleName());
                u.setBranch(new ArrayList<Branch>());
                u.setEmail(u.getRole().getRoleName() + "@admin.com");
                u.setStatus(Status.ACTIVE);
                u.setIsDefaultCommittee(true);
                userService.save(u);
                logger.info("saving committee default user");

            }
        }
        return r;
    }

    @Override
    public Page<Role> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<Role> saveAll(List<Role> list) {
        return roleRepository.saveAll(list);
    }

    @Override
    public Map<Object, Object> roleStatusCount() {
        return roleRepository.roleStatusCount();
    }

    @Override
    public List<Map<Object, Object>> activeRole() {
        return roleRepository.activeRole();
    }

    @Override
    public boolean isMaker() {
        return roleRepository.chkByRoleType(RoleType.MAKER);
    }

    @Override
    public Role getRoleCAD() {
        return roleRepository.getRoleCAD();
    }

    @Override
    public List<Role> getByRoleTypeAndStatus(RoleType roleType, Status status) {
        return roleRepository.getAllByRoleTypeEqualsAndStatus(roleType, Status.ACTIVE);
    }
}
