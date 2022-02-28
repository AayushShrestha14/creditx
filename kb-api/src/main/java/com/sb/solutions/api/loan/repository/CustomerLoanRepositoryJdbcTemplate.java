package com.sb.solutions.api.loan.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.loan.constant.CustomerLoanSqlConstants;
import com.sb.solutions.api.loan.dto.CustomerLoanDto;
import com.sb.solutions.core.enums.DocStatus;

/**
 * @author : Rujan Maharjan on  9/28/2020
 **/

@Repository
public class CustomerLoanRepositoryJdbcTemplate {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public CustomerLoanRepositoryJdbcTemplate(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

    }

    public List<CustomerLoanDto> findByLoanHolderCustomerGroupAndNotToCurrentLoanHolder(
        Long gId, Long lId) {
        Map<String, Object> map = new HashMap<>();
        map.put("gId", gId);
        map.put("lId", lId);
        String query = CustomerLoanSqlConstants.GET_LOAN_BY_GROUP_ID_AND_NOT_IN_CURRENT_LOAN_HOLDER;
        return customerLoanDtoList(namedParameterJdbcTemplate.queryForList(query, map));

    }

    private List<CustomerLoanDto> customerLoanDtoList(List<Map<String, Object>> list) {
        List<CustomerLoanDto> customerLoanDtoList = new ArrayList<>();
        list.forEach(l -> {
            CustomerLoanDto c = new CustomerLoanDto();
            c.setGroupId(l.get("groupId") == null ? null : l.get("groupId").toString());
            c.setGroupCode(l.get("groupCode") == null ? null : l.get("groupCode").toString());
            c.setLoanHolderId(
                l.get("loanHolderId") == null ? null : l.get("loanHolderId").toString());
            c.setName(l.get("customerName") == null ? null : ((String) l.get("customerName")));
            c.setGroupLimit(l.get("groupLimit") == null ? null : (l.get("groupLimit").toString()));
            c.setProposedLimit(
                l.get("proposedLimit") == null ? null : (l.get("proposedLimit").toString()));
            c.setCustomerLoanId(
                l.get("customerLoanId") == null ? null : (l.get("customerLoanId").toString()));
            Integer status = Integer.parseInt(l.get("documentStatus").toString());
            c.setDocumentStatus(DocStatus.values()[status]);
            customerLoanDtoList.add(c);
        });
        return customerLoanDtoList;
    }

}
