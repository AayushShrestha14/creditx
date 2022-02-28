package com.sb.solutions.api.authorization.approval;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.sb.solutions.api.authorization.service.RoleService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.authorization.service.RoleHierarchyService;
import com.sb.solutions.core.enums.RoleType;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.utils.ApprovalType;

@Service
public class ApprovalRoleHierarchyServiceImpl implements ApprovalRoleHierarchyService {

    private final RoleHierarchyService defaultRoleHierarchyService;
    private final ApprovalRoleHierarchyRepository repository;
    private final RoleService roleService;

    public ApprovalRoleHierarchyServiceImpl(
        RoleHierarchyService defaultRoleHierarchyService,
        ApprovalRoleHierarchyRepository repository, RoleService roleService) {
        this.defaultRoleHierarchyService = defaultRoleHierarchyService;
        this.repository = repository;
        this.roleService = roleService;
    }

    @Override
    public List<ApprovalRoleHierarchy> getForwardRolesForRole(Long roleId,
        ApprovalType approvalType, Long typeId,
        ApprovalType refType, Long refId) {
        final List<ApprovalRoleHierarchy> referenceBasedHierarchies = repository
            .findAllByApprovalTypeEqualsAndRefId(refType, refId);

        if (CollectionUtils.isNotEmpty(referenceBasedHierarchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = referenceBasedHierarchies.stream()
                .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            return currentRole.map(hierarchy -> referenceBasedHierarchies.stream().filter(
                h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() < hierarchy
                    .getRoleOrder()).collect(toList())).orElse(referenceBasedHierarchies);
        }

        final List<ApprovalRoleHierarchy> typeBasedHierarchies = repository
            .findAllByApprovalTypeEqualsAndRefId(approvalType, typeId);

        if (CollectionUtils.isNotEmpty(typeBasedHierarchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = typeBasedHierarchies.stream()
                .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            final List<ApprovalRoleHierarchy> selected = currentRole
                .map(hierarchy -> typeBasedHierarchies.stream().filter(
                    h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() < hierarchy
                        .getRoleOrder()).collect(toList())).orElse(typeBasedHierarchies);

            return selected;
        }

        return defaultRoleHierarchyService.roleHierarchyByCurrentRoleForward(roleId).stream()
            .map(r -> {
                ApprovalRoleHierarchy hierarchy = new ApprovalRoleHierarchy();
                hierarchy.setApprovalType(ApprovalType.DEFAULT);
                hierarchy.setRefId(0L);
                hierarchy.setRoleOrder(r.getRoleOrder());
                Role role = new Role();
                role.setId(r.getId());
                role.setRoleName(r.getRoleName());
                hierarchy.setRole(r.getRole());

                return hierarchy;
            }).collect(toList());
    }

    @Override
    public List<ApprovalRoleHierarchy> getForwardRolesForRoleWithType(Long roleId,
        ApprovalType approvalType, Long refId) {

        final List<ApprovalRoleHierarchy> typeBasedHierarchies = repository
            .findAllByApprovalTypeEqualsAndRefId(approvalType, refId);

        if (CollectionUtils.isNotEmpty(typeBasedHierarchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = typeBasedHierarchies.stream()
                .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            return currentRole.map(hierarchy -> typeBasedHierarchies.stream().filter(
                h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() < hierarchy
                    .getRoleOrder()).collect(toList())).orElse(typeBasedHierarchies);
        }

        return defaultRoleHierarchyService.roleHierarchyByCurrentRoleForward(roleId).stream()
                .map(r -> {
                    ApprovalRoleHierarchy hierarchy = new ApprovalRoleHierarchy();
                    hierarchy.setApprovalType(ApprovalType.DEFAULT);
                    hierarchy.setRefId(0L);
                    hierarchy.setRoleOrder(r.getRoleOrder());
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setRoleName(r.getRoleName());
                    role.setRoleType(r.getRoleType());
                    hierarchy.setRole(role);

                return hierarchy;
            }).collect(toList());
    }

    @Override
    public List<ApprovalRoleHierarchy> getBackwardRolesForRole(Long roleId,
                                                               ApprovalType approvalType, Long typeId, ApprovalType refType,
                                                               Long refId) {

        final List<ApprovalRoleHierarchy> referneceBasedHierarchies = repository
                .findAllByApprovalTypeEqualsAndRefId(refType, refId);

        if (CollectionUtils.isNotEmpty(referneceBasedHierarchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = referneceBasedHierarchies.stream()
                    .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            return currentRole.map(hierarchy -> referneceBasedHierarchies.stream().filter(
                    h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() > hierarchy
                            .getRoleOrder()).collect(toList())).orElse(referneceBasedHierarchies);
        }

        final List<ApprovalRoleHierarchy> typeBasedHiererchies = repository
                .findAllByApprovalTypeEqualsAndRefId(approvalType, typeId);

        if (CollectionUtils.isNotEmpty(typeBasedHiererchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = typeBasedHiererchies.stream()
                    .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            return currentRole.map(hierarchy -> typeBasedHiererchies.stream().filter(
                    h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() > hierarchy
                            .getRoleOrder()).collect(toList())).orElse(typeBasedHiererchies);
        }

        return defaultRoleHierarchyService.roleHierarchyByCurrentRoleBackward(roleId).stream()
                .map(r -> {
                    ApprovalRoleHierarchy hierarchy = new ApprovalRoleHierarchy();
                    hierarchy.setApprovalType(ApprovalType.DEFAULT);
                    hierarchy.setRefId(0L);
                    hierarchy.setRoleOrder(r.getRoleOrder());
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setRoleName(r.getRoleName());
                    hierarchy.setRole(r.getRole());

                    return hierarchy;
                }).collect(toList());
    }

    @Override
    public List<ApprovalRoleHierarchy> getBackwardRolesForRoleWithType(Long roleId,
                                                                       ApprovalType approvalType, Long refId) {
        final List<ApprovalRoleHierarchy> referneceBasedHierarchies = repository
                .findAllByApprovalTypeEqualsAndRefId(approvalType, refId);

        if (CollectionUtils.isNotEmpty(referneceBasedHierarchies)) {
            final Optional<ApprovalRoleHierarchy> currentRole = referneceBasedHierarchies.stream()
                    .filter(h -> roleId.equals(h.getRole().getId())).findFirst();

            return currentRole.map(hierarchy -> referneceBasedHierarchies.stream().filter(
                    h -> !roleId.equals(h.getRole().getId()) && h.getRoleOrder() > hierarchy
                            .getRoleOrder()).collect(toList())).orElse(referneceBasedHierarchies);
        }

        return defaultRoleHierarchyService.roleHierarchyByCurrentRoleBackward(roleId).stream()
                .map(r -> {
                    ApprovalRoleHierarchy hierarchy = new ApprovalRoleHierarchy();
                    hierarchy.setApprovalType(ApprovalType.DEFAULT);
                    hierarchy.setRefId(0L);
                    hierarchy.setRoleOrder(r.getRoleOrder());
                    Role role = new Role();
                    role.setId(r.getId());
                    role.setRoleName(r.getRoleName());
                    hierarchy.setRole(r.getRole());

                    return hierarchy;
                }).collect(toList());
    }

    @Override
    public List<ApprovalRoleHierarchy> getRoles(ApprovalType approvalType, Long refId) {
        final List<ApprovalRoleHierarchy> hierarchies = repository
                .findAllByApprovalTypeEqualsAndRefId(approvalType, refId);

        if (CollectionUtils.isNotEmpty(hierarchies)) {
            return hierarchies;
        }

        return getDefaultRoleHierarchies(approvalType, refId);
    }

    @Override
    public List<ApprovalRoleHierarchy> getDefaultRoleHierarchies(ApprovalType approvalType,
                                                                 Long refId) {

        List<ApprovalRoleHierarchy> approvalRoleHierarchyList = defaultRoleHierarchyService.findAll().stream().map(r -> {
            ApprovalRoleHierarchy hierarchy = new ApprovalRoleHierarchy();
            hierarchy.setApprovalType(approvalType);
            hierarchy.setRefId(refId);
            hierarchy.setRoleOrder(r.getRoleOrder());
            hierarchy.setRole(r.getRole());

            return hierarchy;
        }).collect(toList());

        if (approvalType.equals(ApprovalType.CAD)) {
            AtomicReference<Long> orders = new AtomicReference<>(1000L);
            final Role role = roleService.getRoleCAD();
            ApprovalRoleHierarchy approvalRoleHierarchy = new ApprovalRoleHierarchy();
            approvalRoleHierarchy.setApprovalType(approvalType);
            approvalRoleHierarchy.setRefId(refId);
            approvalRoleHierarchy.setRoleOrder(orders.get());
            approvalRoleHierarchy.setRole(role);
            approvalRoleHierarchyList.add(approvalRoleHierarchy);

            final List<Role> roleList = roleService.getByRoleTypeAndStatus(RoleType.CAD_LEGAL,
                Status.ACTIVE);
            roleList.forEach(r -> {
                orders.set(orders.get() + 1L);
                ApprovalRoleHierarchy approvalRoleHierarchy1 = new ApprovalRoleHierarchy();
                approvalRoleHierarchy1.setApprovalType(approvalType);
                approvalRoleHierarchy1.setRefId(refId);
                approvalRoleHierarchy1.setRoleOrder(orders.get());
                approvalRoleHierarchy1.setRole(r);
                approvalRoleHierarchyList.add(approvalRoleHierarchy1);
            });
        }
        return approvalRoleHierarchyList;
    }

    @Override
    public boolean checkRoleContainInHierarchies(Long id, ApprovalType approvalType, Long refId) {
        return repository.existsByApprovalTypeAndRefIdAndRoleId(approvalType, refId, id);
    }


    @Override
    public List<ApprovalRoleHierarchy> findAll() {
        return repository.findAll();
    }

    @Override
    public ApprovalRoleHierarchy findOne(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public ApprovalRoleHierarchy save(ApprovalRoleHierarchy approvalRoleHierarchy) {
        return null;
    }

    @Override
    public Page<ApprovalRoleHierarchy> findAllPageable(Object t, Pageable pageable) {
        return null;
    }

    @Override
    public List<ApprovalRoleHierarchy> saveAll(List<ApprovalRoleHierarchy> list) {
        final ApprovalRoleHierarchy hierarchyType = list.get(0);

        final List<ApprovalRoleHierarchy> available = repository
            .findAllByApprovalTypeEqualsAndRefId(
                hierarchyType.getApprovalType(),
                hierarchyType.getRefId());

        if (isFresh(hierarchyType)) {
            repository.deleteInBatch(available);
        } else {
            final List<Long> selectedRoles = list.stream().map(a -> a.getRole().getId())
                .collect(toList());

            final List<ApprovalRoleHierarchy> deselectedRoles = available.stream()
                .filter(a -> !selectedRoles.contains(a.getRole().getId())).collect(toList());

            repository.deleteInBatch(deselectedRoles);
        }

        return repository.saveAll(list);
    }

    private boolean isFresh(ApprovalRoleHierarchy hierarchy) {
        return hierarchy.getId() == null || hierarchy.getId() < 1;
    }
}
