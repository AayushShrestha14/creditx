package com.sb.solutions.api.approvallimit.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sb.solutions.api.approvallimit.emuns.LoanApprovalType;
import com.sb.solutions.api.approvallimit.entity.ApprovalLimit;
import com.sb.solutions.api.approvallimit.repository.ApprovalLimitRepository;
import com.sb.solutions.api.approvallimit.repository.specification.ApprovalLimitSpecBuilder;
import com.sb.solutions.core.exception.ServiceValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ApprovalLimitServiceImpl implements ApprovalLimitService {

    private final ApprovalLimitRepository approvalLimitRepository;

    public ApprovalLimitServiceImpl(
        @Autowired ApprovalLimitRepository approvalLimitRepository) {
        this.approvalLimitRepository = approvalLimitRepository;
    }

    @Override
    public List<ApprovalLimit> findAll() {
        return approvalLimitRepository.findAll();
    }

    @Override
    public ApprovalLimit findOne(Long id) {
        return null;
    }

    @Override
    public ApprovalLimit save(ApprovalLimit approvalLimit) {
        ApprovalLimit approvalLimit2 = approvalLimitRepository
            .getByAuthoritiesIdAndLoanCategoryIdAndLoanApprovalType(
                approvalLimit.getAuthorities().getId(),
                approvalLimit.getLoanCategory().getId(),
                approvalLimit.getLoanApprovalType());

        if (approvalLimit.getId() == null) {
            if (approvalLimit2 != null) {
                throw new ServiceValidationException(String.format(
                    "Already Exist : Approval Limit Already Exist of Role : %s ,Loan category : %s  and Approval Type : %s",
                    approvalLimit2.getAuthorities().getRoleName(),
                    approvalLimit2.getLoanCategory().getName(),
                    approvalLimit2.getLoanApprovalType()));
            }
        } else {
            if (!ObjectUtils.isEmpty(approvalLimit2) && (!Objects
                .equals(approvalLimit2.getId(), approvalLimit.getId()))) {
                throw new ServiceValidationException(String.format(
                    "Already Exist : Approval Limit Already Exist of Role : %s ,Loan category : %s  and Approval Type : %s",
                    approvalLimit2.getAuthorities().getRoleName(),
                    approvalLimit2.getLoanCategory().getName(),
                    approvalLimit2.getLoanApprovalType()));

            }

        }
        return approvalLimitRepository.save(approvalLimit);


    }

    @Override
    public Page<ApprovalLimit> findAllPageable(Object object, Pageable pageable) {
        final ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> stringMap = objectMapper.convertValue(object, Map.class);
        final ApprovalLimitSpecBuilder approvalLimitSpecBuilder = new ApprovalLimitSpecBuilder(stringMap);
        final Specification<ApprovalLimit> specification = approvalLimitSpecBuilder.build();
        return approvalLimitRepository.findAll(specification, pageable);
    }

    @Override
    public List<ApprovalLimit> saveAll(List<ApprovalLimit> list) {
        return approvalLimitRepository.saveAll(list);
    }

    @Override
    public ApprovalLimit getByRoleAndLoan(Long roleId, Long loanConfigId,
        LoanApprovalType loanCategory) {
        return approvalLimitRepository
            .getByAuthoritiesIdAndLoanCategoryIdAndLoanApprovalType(roleId, loanConfigId,
                loanCategory);
    }
}
