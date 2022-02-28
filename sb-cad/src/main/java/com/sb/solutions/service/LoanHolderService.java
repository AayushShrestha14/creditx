package com.sb.solutions.service;

import com.sb.solutions.dto.CadStageDto;
import com.sb.solutions.dto.LoanHolderDto;
import com.sb.solutions.entity.CustomerApprovedLoanCadDocumentation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

/**
 * @author : Rujan Maharjan on  12/1/2020
 **/

public interface LoanHolderService {

    Page<LoanHolderDto> getAllUnAssignLoanForCadAdmin(Map<String, String> filterParams,
        Pageable pageable);

    @Transactional
    String assignLoanToUser(CadStageDto cadStageDto);

    String cadAction(CadStageDto cadStageDto);

    CustomerApprovedLoanCadDocumentation getByID(Long id);

    Page<?> getAllByFilterParams(
        Map<String, String> filterParams, Pageable pageable);

    Map<String , Object> getCadDocumentCount(Map<String , String> param);

    @Transactional
    String assignCadToUser(CadStageDto cadStageDto);

    @Transactional
    String saveAdditionalDisbursement(CustomerApprovedLoanCadDocumentation c, Long roleId);

    List<Map<String,Object>>  getStat();
}
