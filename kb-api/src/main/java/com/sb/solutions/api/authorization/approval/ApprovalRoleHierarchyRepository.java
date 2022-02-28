package com.sb.solutions.api.authorization.approval;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sb.solutions.core.utils.ApprovalType;

@Repository
public interface ApprovalRoleHierarchyRepository extends
    JpaRepository<ApprovalRoleHierarchy, Long> {

    List<ApprovalRoleHierarchy> findAllByApprovalTypeEqualsAndRefId(
        ApprovalType approvalType,
        Long refId);

//    @Query(value = "SELECT CASE WHEN EXISTS (select * from approval_role_hierarchy where approval_type=:approvalType and ref_id=:refId and role_id=:roleId) THEN CAST(1 AS BIT) " +
//            "ELSE CAST(0 AS BIT) END",nativeQuery = true)
    boolean existsByApprovalTypeAndRefIdAndRoleId(ApprovalType approvalType,Long refId,Long roleId);

}
