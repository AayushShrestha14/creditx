package com.sb.solutions.api.loan.service;

import com.sb.solutions.api.authorization.approval.ApprovalRoleHierarchy;
import com.sb.solutions.api.authorization.dto.RoleDto;
import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.core.dto.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loan.entity.CustomerOfferLetter;
import com.sb.solutions.core.service.BaseService;

import java.util.List;
import java.util.Map;

public interface CustomerOfferService extends BaseService<CustomerOfferLetter> {

    CustomerOfferLetter findByCustomerLoanId(Long id);

    @Transactional
    CustomerOfferLetter action(CustomerOfferLetter stageDto);

    Page<CustomerLoan> getIssuedOfferLetter(Object searchDto, Pageable pageable);

    @Transactional
    CustomerOfferLetter saveWithMultipartFile(MultipartFile multipartFile, Long customerLoanId,
        Long offerLetterId,String type);

    @Transactional
    CustomerOfferLetter assignOfferLetter(Long customerLoanId,Long userId,Long roleId);

    Page<CustomerOfferLetter> getAssignedOfferLetter(Object searchDto, Pageable pageable);

    Map<String,Object> userPostApprovalDocStat();

    List<RoleDto>  getUserListForFilter(List<ApprovalRoleHierarchy> approvalRoleHierarchies, SearchDto searchDto);

}
