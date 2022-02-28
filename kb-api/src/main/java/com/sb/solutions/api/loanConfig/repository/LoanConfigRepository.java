package com.sb.solutions.api.loanConfig.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 2/26/2019
 */
public interface LoanConfigRepository extends JpaRepository<LoanConfig, Long> {

    @Query(value = "select"
        + "         (select  count(id) from loan_config where status=1) active,"
        + "            (select  count(id) from loan_config where status=0) inactive,"
        + "            (select  count(id) from loan_config) loans", nativeQuery = true)
    Map<Object, Object> loanStatusCount();

    @Query(value = "select l from LoanConfig l where l.name like  concat(:name,'%')")
    Page<LoanConfig> loanConfigFilter(@Param("name") String name, Pageable pageable);

    List<LoanConfig> getByStatus(Status status);

    List<LoanConfig> findAllByEnableEligibility(boolean enableEligibility);

    LoanConfig findLoanConfigByIdAndEnableEligibility(Long loanConfigId, Boolean enableEligibility);

    List<LoanConfig> findAllByLoanCategoryAndStatus(LoanApprovalType approvalType, Status status);
}
