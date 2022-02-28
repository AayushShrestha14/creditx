package com.sb.solutions.api.loanConfig.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.service.BaseService;

/**
 * @author Rujan Maharjan on 2/26/2019
 */
public interface LoanConfigService extends BaseService<LoanConfig> {

    Map<Object, Object> loanStatusCount();

    List<LoanConfig> getAllByStatus(Status status);

    List<LoanConfig> getLoanConfigsActivatedForEligibility();

    LoanConfig getLoanConfigActivatedForEligibility(Long loanConfigId);

    List<LoanConfig> getByLoanCategoryAndStatus(LoanApprovalType loanApprovalType);
}
