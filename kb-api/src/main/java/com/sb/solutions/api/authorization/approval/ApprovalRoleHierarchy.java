package com.sb.solutions.api.authorization.approval;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.utils.ApprovalType;

@Entity
@Table(name = "approval_role_hierarchy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApprovalRoleHierarchy extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private Long roleOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_type")
    private ApprovalType approvalType;

    private Long refId;
}
