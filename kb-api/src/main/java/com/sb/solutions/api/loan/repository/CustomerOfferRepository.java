package com.sb.solutions.api.loan.repository;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sb.solutions.api.loan.entity.CustomerOfferLetter;

import java.util.List;
import java.util.Map;

public interface CustomerOfferRepository extends JpaRepository<CustomerOfferLetter, Long>,
    JpaSpecificationExecutor<CustomerOfferLetter> {

    CustomerOfferLetter findByCustomerLoanId(Long id);

    Long countCustomerOfferLetterByOfferLetterStageToUserIdAndOfferLetterStageToRoleId(Long userId,Long roleId);

    List<CustomerOfferLetter>  findCustomerOfferLetterByCustomerLoanIn(List<CustomerLoan> customerLoanList);
}
