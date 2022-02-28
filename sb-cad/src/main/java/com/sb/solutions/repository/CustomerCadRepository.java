package com.sb.solutions.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sb.solutions.core.repository.BaseRepository;
import com.sb.solutions.dto.CadAssignedLoanDto;
import com.sb.solutions.entity.CadStage;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;
import com.sb.solutions.enums.CadDocStatus;

/**
 * @author : Rujan Maharjan on  12/7/2020
 **/
public interface CustomerCadRepository extends
    BaseRepository<CustomerApprovedLoanCadDocumentation, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE CustomerApprovedLoanCadDocumentation  c set c.cadStageList= :previousList,c.docStatus=:docStatus , c.cadCurrentStage = :cadStage WHERE c.id = :id")
    void updateAction(@Param("id") Long id, @Param("docStatus") CadDocStatus docStatus,
        @Param("cadStage") CadStage cadStage, @Param("previousList") String previousList);

    @Query(value = "SELECT assigned_loan_id from assigned_loan", nativeQuery = true)
    List<Long> findAllAssignedLoanIds();

    @Query(value = "SELECT new com.sb.solutions.dto.CadAssignedLoanDto(a.currentStage,a.loanType,c.id,a.id,a.proposal,a.loan.name,a.loan.id,a.previousStageList) FROM CustomerApprovedLoanCadDocumentation c inner join c.assignedLoan a  where c.id in (:ids)")
    List<CadAssignedLoanDto> findAssignedLoanByIdIn(@Param("ids") List<Long> ids);
    
    @Query(value = "select u.user_name, "
        + "       u.name, "
        + "       r.role_name as roleName, "
        + "       sum(IIF(doc_status = 0 and "
        + "               cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "               cast(datediff(day, 0, getdate()) as DATETIME), 1, 0))                    AS offerAssignedToday, "
        + "       sum(IIF(doc_status = 0 and "
        + "               cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "               cast(dateadd(day,-1, getdate()) as DATETIME), 1, 0))                    AS offerAssignedYesterday, "
        + "       sum(IIF(doc_status = 3 and "
        + "               cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "               cast(datediff(day, 0, getdate()) as DATETIME), 1, 0))                    AS offerCompleted, "
        + "       sum(IIF(doc_status = 2 and cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "                                  cast(datediff(day, 0, getdate()) as DATETIME), 1, 0)) AS disbursementAssignedToday, "
        + "       sum(IIF(doc_status = 2 and "
        + "               cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "               cast(dateadd(day,-1, getdate()) as DATETIME), 1, 0))                    AS disbursementAssignedYesterday, "
        + "       sum(IIF(doc_status = 5 and "
        + "               cast(datediff(day, 0, c.last_modified_at) as datetime) = "
        + "               cast(datediff(day, 0, getdate()) as DATETIME), 1, 0))                    AS disbursementCompleted "
        + "from customer_approved_loan_cad_documentation c "
        + "         left join cad_stage cs on c.cad_current_stage_id = cs.id "
        + "         left join users u on cs.to_user_id = u.id "
        + "            left join role r on r.id = u.role_id "
        + "where user_name is not null "
        + "group by u.user_name, u.name,r.role_name;",nativeQuery = true)
    List<Map<String,Object>>  getStat();
}
