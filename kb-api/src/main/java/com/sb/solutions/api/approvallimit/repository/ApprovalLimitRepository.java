package com.sb.solutions.api.approvallimit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;

public interface ApprovalLimitRepository extends JpaRepository<ApprovalLimit, Long>,
        JpaSpecificationExecutor<ApprovalLimit> {

    @Query(value = "select a from ApprovalLimit a")
    Page<ApprovalLimit> approvalLimitFilter(Pageable pageable);

    ApprovalLimit getByAuthoritiesIdAndLoanCategoryIdAndLoanApprovalType(Long id, Long loanConfigId,
                                                                         LoanApprovalType loanCategory);
}
