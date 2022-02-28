package com.sb.solutions.api.loan.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.loan.constant.CustomerLoanSqlConstants;
import com.sb.solutions.api.loan.dto.CustomerApprovedLoanDto;

/**
 * @author : Rujan Maharjan on  3/8/2021
 **/
@Repository
@Slf4j
public class CustomerApprovedLoanDaoImpl implements CustomerApprovedLoanDao {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomerApprovedLoanDaoImpl(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CustomerApprovedLoanDto> getCADLoanInCurrentUser(Long id, List<Long> ids) {
        List<CustomerApprovedLoanDto> finalList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("uId", ids);
        map.put("roleIDs", ids);
        String query = CustomerLoanSqlConstants.GET_CAD_IN_USER_IDS;
        try {
            List<Map<String, Object>> list = namedParameterJdbcTemplate.queryForList(query, map);

            list.forEach(obj -> {
                Gson gson = new GsonBuilder().create();
                final CustomerApprovedLoanDto currentRow = gson
                    .fromJson(gson.toJson(obj), CustomerApprovedLoanDto.class);
                finalList.add(currentRow);
            });
        } catch (Exception e) {
            log.error("unable to fetch data!");
        }

        return finalList;

    }

}
