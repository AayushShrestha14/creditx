package com.sb.solutions.repository.customRepo;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.sb.solutions.dto.LoanHolderDto;

/**
 * @author : Rujan Maharjan on  9/17/2021
 **/
@Component
public class JDBCQuery {


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private ObjectMapper objectMapper = new ObjectMapper()
        .setDateFormat(new SimpleDateFormat("YYYY-MM-dd"))
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public JDBCQuery(
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public List<LoanHolderDto> getUnassignedLoan(
        String query) {
        List<Map<String, Object>> list = namedParameterJdbcTemplate
            .queryForList(query, new HashMap<>());
        return mapLoanHolder(list);
    }

    public Long getUnassignedLoanTotalCount(
        String countQuery) {
        Long count = namedParameterJdbcTemplate
            .queryForObject(countQuery, new HashMap<>(), Long.class);
        return count;
    }


    private List<LoanHolderDto> mapLoanHolder(List<Map<String, Object>> maps) {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.convertValue(maps,
            typeFactory.constructCollectionType(List.class, LoanHolderDto.class));
    }
}
