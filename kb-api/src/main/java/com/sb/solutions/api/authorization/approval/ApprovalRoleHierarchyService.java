package com.sb.solutions.api.authorization.approval;

import java.util.List;

import com.sb.solutions.core.service.BaseService;
import com.sb.solutions.core.utils.ApprovalType;

public interface ApprovalRoleHierarchyService extends BaseService<ApprovalRoleHierarchy> {

    List<ApprovalRoleHierarchy> getForwardRolesForRole(Long roleId,
        ApprovalType approvalType, Long typeId,
        ApprovalType refType, Long refId);

    List<ApprovalRoleHierarchy> getForwardRolesForRoleWithType(Long roleId,
        ApprovalType approvalType, Long refId);

    List<ApprovalRoleHierarchy> getBackwardRolesForRole(Long roleId,
        ApprovalType approvalType, Long typeId,
        ApprovalType refType, Long refId);

    List<ApprovalRoleHierarchy> getBackwardRolesForRoleWithType(Long roleId,
        ApprovalType approvalType, Long refId);

    List<ApprovalRoleHierarchy> getRoles(ApprovalType approvalType, Long refId);
    List<ApprovalRoleHierarchy> getDefaultRoleHierarchies(ApprovalType approvalType, Long refId);

    boolean checkRoleContainInHierarchies(Long id,ApprovalType approvalType,Long refId);
}
