package com.sb.solutions.api.loan.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.customer.entity.Customer;
import com.sb.solutions.api.customerGroup.CustomerGroup;
import com.sb.solutions.api.customerRelative.entity.CustomerRelative;
import com.sb.solutions.api.guarantor.entity.Guarantor;
import com.sb.solutions.api.loan.PieChartDto;
import com.sb.solutions.api.loan.StatisticDto;
import com.sb.solutions.api.loan.dto.CustomerInfoLoanDto;
import com.sb.solutions.api.loan.dto.GroupSummaryDto;
import com.sb.solutions.api.loan.entity.CadDocument;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.service.BaseService;
import com.sb.solutions.report.core.service.FormReportGeneratorService;

/**
 * @author Rujan Maharjan on 6/4/2019
 */
public interface CustomerLoanService extends BaseService<CustomerLoan>, FormReportGeneratorService {

    @Transactional
    void sendForwardBackwardLoan(CustomerLoan customerLoan);

    @Transactional
    void sendForwardBackwardLoan(List<CustomerLoan> customerLoans);

    Map<String, Integer> statusCount();

    List<CustomerLoan> getFirst5CustomerLoanByDocumentStatus(DocStatus status);


    List<PieChartDto> proposedAmount(String startDate, String endDate) throws ParseException;

    List<PieChartDto> proposedAmountByBranch(Long branchId, String startDate, String endDate)
        throws ParseException;

    List<CustomerLoan> getByCitizenshipNumber(String citizenshipNumber);

    Page<CustomerLoan> getCatalogues(Object searchDto, Pageable pageable);

    Page<CustomerLoan> getCommitteePull(Object searchDto, Pageable pageable);

    Page<CustomerLoan> getIssuedOfferLetter(Object searchDto, Pageable pageable);

    CustomerLoan delCustomerLoan(Long id);

    List<StatisticDto> getStats(Long branchId, String startDate, String endDate)
        throws ParseException;

    Map<String, String> chkUserContainCustomerLoan(Long id);

    @Transactional
    CustomerLoan renewCloseEntity(CustomerLoan object);

    String csv(Object searchDto);

    List<CustomerLoan> getLoanByCustomerId(Long id);

    List<CustomerLoan> getLoanByLoanHolderId(Long id);

    List<CustomerLoan> getAllLoansByLoanHolderId(Long loanHolderId);

    List<CustomerLoan> getFinalUniqueLoansById(Long id);

    List<CustomerLoan> getLoanByCustomerKycGroup(CustomerRelative customerRelative);

    GroupSummaryDto getLoanByCustomerGroup(CustomerGroup customerGroup);

    Page<Customer> getCustomerFromCustomerLoan(Object searchDto, Pageable pageable);

    List<CustomerLoan> getLoanByCustomerGuarantor(Guarantor guarantor);

    @Transactional
    void postLoanConditionCheck(CustomerLoan loan);

    List<CustomerLoan> findAllBySpec(Map<String, String> filterParams);

    List<CustomerLoan> findByCombinedLoanId(long id);

    CustomerLoan saveCadLoanDocument(Long loanId, List<CadDocument> cadDocuments, String data);

    CustomerLoan saveCbsNumbers(CustomerLoan loan);

    /**
     * Loan Fetch By customerInfo Entity
     * MEGA BANK SPECIFIC
     *
     * @param searchDto
     * @param pageable
     * @return
     */
    Page<CustomerInfoLoanDto> getLoanByCustomerInfo(Object searchDto, Pageable pageable);
    Page<CustomerInfoLoanDto> getLoanByCustomerInfoCommitteePULL(Object searchDto, Pageable pageable);

    Boolean checkCustomerIsEditable(Long loanHolderId);

    void changeLoan(Long customerLoanId,Long loanConfigId);

    @Transactional
    void transferLoanToOtherBranch(CustomerLoan customerLoan, Long branchId, User currentUser);

    @Transactional
    void deleteLoanByMakerAndAdmin(Long customerLoanId);

    @Transactional
    CustomerLoan saveLoan(CustomerLoan customerLoan, User loggedInUser);
}
