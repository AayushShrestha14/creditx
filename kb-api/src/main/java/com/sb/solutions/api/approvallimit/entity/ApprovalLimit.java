package com.sb.solutions.api.approvallimit.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApprovalLimit extends BaseEntity<Long> {

    private static final long serialVersionUID = 1L;

    private BigDecimal amount;
    @Column(nullable = true)
    private LoanApprovalType loanApprovalType;
    @OneToOne
    private LoanConfig loanCategory;
    @OneToOne
    @JoinColumn(name = "authorities_id")
    private Role authorities;


}
