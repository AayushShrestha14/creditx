package com.sb.solutions.api.approvallimit.service;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.core.service.BaseService;

public interface ApprovalLimitService extends BaseService<ApprovalLimit> {

    ApprovalLimit getByRoleAndLoan(Long roleId, Long loanConfigId,
                                   LoanApprovalType loanCategory);
}
