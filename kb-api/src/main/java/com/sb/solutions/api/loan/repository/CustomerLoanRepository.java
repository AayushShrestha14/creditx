package com.sb.solutions.api.loan.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sb.solutions.api.authorization.entity.Role;
import com.sb.solutions.api.loan.LoanStage;
import com.sb.solutions.api.loan.PieChartDto;
import com.sb.solutions.api.loan.StatisticDto;
import com.sb.solutions.api.loan.dto.CustomerLoanFilterDto;
import com.sb.solutions.api.loan.dto.CustomerLoanGroupDto;
import com.sb.solutions.api.loan.dto.LoanStageDto;
import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.user.dto.UserDto;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.enums.DocAction;
import com.sb.solutions.core.enums.DocStatus;
import com.sb.solutions.core.enums.PostApprovalAssignStatus;
import com.sb.solutions.core.enums.RoleType;

/**
 * @author Rujan Maharjan on 6/4/2019
 */
public interface CustomerLoanRepository extends JpaRepository<CustomerLoan, Long>,
    JpaSpecificationExecutor<CustomerLoan> {

    List<CustomerLoan> findFirst5ByDocumentStatusAndCurrentStageToRoleIdAndBranchIdOrderByIdDesc(
        DocStatus status, Long currentStageRoleId, Long branchId);

    @Query(value = "SELECT "
        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status  in(4,5,6,7)  AND l.to_role_id IN (:id)"
        + " AND cl.branch_id IN (:bid) AND l.to_user_id=:uid And cl.child_id IS NULL) initial,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=0 AND l.to_role_id IN (:id)"
        + " AND cl.branch_id IN (:bid) AND l.to_user_id=:uid And cl.child_id IS NULL) pending,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=1"
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Approved,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=2 "
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Rejected,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=3"
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Closed,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl"
        + " LEFT JOIN loan_stage l ON l.id=cl.current_stage_id WHERE "
        + "  cl.branch_id IN (:bid) And cl.child_id IS NULL)total,"

        + "(SELECT COUNT(cl.id) FROM customer_loan cl WHERE cl.notify = 1 AND "
        + "cl.noted_by IS NULL And cl.child_id IS NULL) notify", nativeQuery = true)
    Map<String, Integer> statusCount(@Param("id") Long id, @Param("bid") List<Long> bid,
        @Param("uid") Long uid);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) FROM CustomerLoan c"
            + " join c.loan l WHERE c.branch.id IN (:branchId) And c.childId IS NULL  GROUP BY c.loan.id,l.name")
    List<PieChartDto> proposedAmount(@Param("branchId") List<Long> branchId);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) FROM CustomerLoan c"
            + " join c.loan l WHERE c.branch.id IN (:branchId)  AND c.createdAt <= :endDate And c.childId IS NULL GROUP"
            + " BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountBefore(@Param("branchId") List<Long> branchId,
        @Param("endDate") Date endDate);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) FROM CustomerLoan c"
            + " join c.loan l WHERE c.branch.id IN (:branchId)  AND c.createdAt >= :startDate And c.childId IS NULL GROUP"
            + " BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountAfter(@Param("branchId") List<Long> branchId,
        @Param("startDate") Date startDate);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) FROM CustomerLoan c"
            + " join c.loan l WHERE c.branch.id IN (:branchId) AND (c.createdAt >= "
            + ":startDate AND c.createdAt <= :endDate)  And c.childId IS NULL GROUP BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountBetween(@Param("branchId") List<Long> branchId,
        @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) "
            + "FROM CustomerLoan c join c.loan l WHERE c.branch.id = :branchId And c.childId IS NULL GROUP BY c.loan"
            + ".id, l.name")
    List<PieChartDto> proposedAmountByBranchId(@Param("branchId") Long branchId);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) "
            + "FROM CustomerLoan c join c.loan l WHERE c.branch.id = :branchId AND c.createdAt <="
            + " :endDate  And c.childId IS NULL GROUP BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountByBranchIdAndDateBefore(@Param("branchId") Long branchId,
        @Param("endDate") Date endDate);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) "
            + "FROM CustomerLoan c JOIN c.loan l WHERE c.branch.id = :branchId AND c.createdAt >= "
            + ":startDate  And c.childId IS NULL GROUP BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountByBranchIdAndDateAfter(@Param("branchId") Long branchId,
        @Param("startDate") Date startDate);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.PieChartDto(l.name,SUM(c.proposal.proposedLimit)) "
            + "FROM CustomerLoan c join c.loan l WHERE c.branch.id = :branchId AND (c.createdAt "
            + ">= :startDate AND c.createdAt <= :endDate) And c.childId IS NULL GROUP BY c.loan.id, l.name")
    List<PieChartDto> proposedAmountByBranchIdAndDateBetween(@Param("branchId") Long branchId,
        @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<CustomerLoan> getByCustomerInfoCitizenshipNumber(String citizenshipNumber1);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), "
            + "c.documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id = :branchId  And c.childId IS NULL "
            + "GROUP BY c.loan.id, c.loan.name, c.documentStatus")
    List<StatisticDto> getLasStatisticsByBranchId(@Param("branchId") Long branchId);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), "
            + "c.documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id = :branchId "
            + "AND c.createdAt <= :endDate And c.childId IS NULL GROUP BY c.loan.id, c.loan.name, c.documentStatus")
    List<StatisticDto> getLasStatisticsByBranchIdAndDateBefore(@Param("branchId") Long branchId,
        @Param("endDate") Date endDate);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), "
            + "c.documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id = :branchId "
            + "AND c.createdAt >= :startDate And c.childId IS NULL GROUP BY c.loan.id, c.loan.name, c.documentStatus")
    List<StatisticDto> getLasStatisticsByBranchIdAndDateAfter(@Param("branchId") Long branchId,
        @Param("startDate") Date startDate);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), "
            + "c.documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id = :branchId "
            + "AND (c.createdAt >= :startDate AND c.createdAt <= :endDate)  And c.childId IS NULL GROUP BY c.loan.id, c"
            + ".loan.name, c.documentStatus")
    List<StatisticDto> getLasStatisticsByBranchIdAndDateBetween(@Param("branchId") Long branchId,
        @Param("startDate") Date startDate, @Param("endDate") Date endDate);


    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), c"
            + ".documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id IN "
            + "(:branchIds)  And c.childId IS NULL GROUP BY c.loan.id, c.loan.name, c.documentStatus")
    List<StatisticDto> getLasStatistics(@Param("branchIds") List<Long> branchIds);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), c"
            + ".documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id IN "
            + "(:branchIds) AND c.createdAt <= :endDate And c.childId IS NULL GROUP BY c.loan.id, c.loan.name, c"
            + ".documentStatus")
    List<StatisticDto> getLasStatisticsAndDateBefore(@Param("branchIds") List<Long> branchIds,
        @Param("endDate") Date endDate);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), c"
            + ".documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id IN "
            + "(:branchIds) AND c.createdAt >= :startDate And c.childId IS NULL GROUP BY c.loan.id, c.loan.name, c"
            + ".documentStatus")
    List<StatisticDto> getLasStatisticsAndDateAfter(@Param("branchIds") List<Long> branchIds,
        @Param("startDate") Date startDate);

    @Query(
        "SELECT NEW com.sb.solutions.api.loan.StatisticDto(SUM(c.proposal.proposedLimit), c"
            + ".documentStatus, c.loan.name, COUNT(c)) FROM CustomerLoan c WHERE c.branch.id IN "
            + "(:branchIds) AND (c.createdAt >= :startDate AND c.createdAt <= :endDate) And c.childId IS NULL GROUP BY c.loan.id,"
            + " c.loan.name, c.documentStatus")
    List<StatisticDto> getLasStatisticsAndDateBetween(@Param("branchIds") List<Long> branchIds,
        @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Modifying
    @Transactional
    void deleteByIdAndCurrentStageDocAction(Long id, DocAction docAction);

    @Query("SELECT COUNT(c) FROM CustomerLoan c JOIN c.currentStage s"
        + " WHERE s.toUser.id = :id AND s.toRole.id=:rId AND c.documentStatus= :docStatus")
    Integer chkUserContainCustomerLoan(@Param("id") Long id, @Param("rId") Long rId,
        @Param("docStatus") DocStatus docStatus);

    @Query(value = "SELECT COUNT(CASE WHEN to_user_id = :id AND (doc_status = 0 OR doc_status = 1 OR doc_status = 2) THEN id END) FROM (SELECT c.id, cs.to_user_id, c.doc_status  FROM customer_approved_loan_cad_documentation c JOIN cad_stage cs on cs.id = c.cad_current_stage_id) a", nativeQuery = true)
    Integer chkCadUserContainCustomerLoan(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE CustomerLoan c SET c.isCloseRenew = :isCloseRenew ,c.childId = :cId  WHERE c.id = :id")
    void updateCloseRenewChildId(@Param("cId") Long cId, @Param("id") Long id ,@Param("isCloseRenew")boolean isCloseRenew);

    List<CustomerLoan> getCustomerLoanByCustomerInfoId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE CustomerLoan c SET c.refNo = :refId  WHERE c.id = :id")
    void updateReferenceNo(@Param("refId") String refId, @Param("id") Long id);

    @Query("select c from CustomerLoan c where c.loanHolder.customerGroup.id = :groupId and c.documentStatus <> 2 and c.previousStageList is not null ")
    List<CustomerLoan> getCustomerLoansByDocumentStatusAndCurrentStage(
        @Param("groupId") Long groupId);

    @Query(value =
        "SELECT NEW com.sb.solutions.api.loan.dto.CustomerLoanGroupDto(c.id, c.loanHolder.name , c.loanHolder.id,  c.loanHolder.associateId,"
            + "c.proposal, c.loanHolder.security, c.documentStatus , c.loan) FROM CustomerLoan c where "
            + "c.loanHolder.customerGroup.id = :groupId and c.documentStatus <> 2 and c.previousStageList is not null")
    List<CustomerLoanGroupDto> getGroupDetailByCustomer(@Param("groupId") Long groupId);

    List<CustomerLoan> getCustomerLoanByAndLoanHolderId(Long id);

    List<CustomerLoan> findAllByLoanHolderId(Long loanHolderId);

    List<CustomerLoan> findAllByCombinedLoanId(long id);

    Long countCustomerLoanByDocumentStatus(DocStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE CustomerLoan c SET c.postApprovalAssignStatus=:status,c.postApprovalAssignedUser=:user WHERE c.id = :id")
    void updatePostApprovalAssignedStatus(@Param("status") PostApprovalAssignStatus status,
        @Param("id") Long id, @Param("user") User user);

    @Transactional
    @Query(
        "SELECT c FROM CustomerLoan c WHERE c.loanHolder.id = :loanHolderId and c.previousStageList is not null and c.id not in "
            +
            "(select c.parentId from CustomerLoan c where c.loanHolder.id = :loanHolderId and c.parentId is not null )")
    List<CustomerLoan> findALlUniqueLoanByCustomerId(@Param("loanHolderId") Long loanHolderId);

    @Transactional

    @Query("SELECT c FROM CustomerLoan c WHERE c.loanHolder.id = :loanHolderId and c.childId is null")
    List<CustomerLoan> findCustomerLoansByLoanHolderId(@Param("loanHolderId") Long loanHolderId);


    List<CustomerLoan> getCustomerLoanByCurrentStageToUserPrimaryUserIdAndCurrentStageToRoleInAndDocumentStatusIn(
        Long id, List<Role> roleList,
        List<DocStatus> docStatus);


    @Query(value = "SELECT "
        + "(SELECT  (COUNT(Distinct cl.loan_holder_id)) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status  in(4,5,6,7)  AND l.to_role_id IN (:id)"
        + " AND cl.branch_id IN (:bid) AND l.to_user_id=:uid And cl.child_id IS NULL) initial,"

        + "(SELECT  (COUNT(Distinct cl.loan_holder_id)) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=0 AND l.to_role_id IN (:id)"
        + " AND cl.branch_id IN (:bid) AND l.to_user_id=:uid And cl.child_id IS NULL) pending,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=1"
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Approved,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=2 "
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Rejected,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl LEFT JOIN loan_stage l"
        + " ON l.id=cl.current_stage_id WHERE cl.document_status=3"
        + " AND cl.branch_id IN (:bid) And cl.child_id IS NULL) Closed,"

        + "(SELECT  COUNT(cl.id) FROM customer_loan cl"
        + " LEFT JOIN loan_stage l ON l.id=cl.current_stage_id WHERE "
        + "  cl.branch_id IN (:bid) And cl.child_id IS NULL)total,"

        + "(SELECT COUNT(cl.id) FROM customer_loan cl WHERE cl.notify = 1 AND "
        + "cl.noted_by IS NULL And cl.child_id IS NULL) notify", nativeQuery = true)
    Map<String, Integer> statusCountCustomerWise(@Param("id") Long id, @Param("bid") List<Long> bid,
        @Param("uid") Long uid);

    Boolean existsByLoanHolderIdAndDocumentStatusAndCurrentStageToRoleRoleTypeNot(@Param("loanHolderId") Long loanHolderId,@Param("status") DocStatus status,@Param("roleType") RoleType roleType);

    @Modifying
    @Transactional
    @Query("UPDATE CustomerLoan c SET c.loan = :loanConfig,c.currentStage=:currentStage,c.previousStageList=:previousList   WHERE c.id = :id")
    void updateLoanConfigByCustomerLoanId(@Param("loanConfig") LoanConfig loanConfig, @Param("id") Long id,@Param("currentStage")LoanStage currentStage,@Param("previousList")String previousList);

    @Query("SELECT new com.sb.solutions.api.loan.dto.CustomerLoanFilterDto(c.id,c.loan.name,c.loanType,c.documentStatus,c.currentStage.toUser.name,c.currentStage.lastModifiedAt,c.priority,c.previousStageList,c.combinedLoan,c.proposal.proposedLimit,c.loan.id,c.loanHolder.id) FROM CustomerLoan c LEFT JOIN c.combinedLoan WHERE c.id in (:id)")
    List<CustomerLoanFilterDto>  getCustomerLoanFilterDtoINLoan(@Param("id")   List<Long>  id);
}
