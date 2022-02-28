package com.sb.solutions.api.cbsGroup.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import com.sb.solutions.api.cbsGroup.config.DataSourceConfig;
import com.sb.solutions.api.cbsGroup.constants.CbsConstant;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.ProductUtils;

/**
 * @author : Rujan Maharjan on  12/20/2020
 * This repository is use to fetch data from remote connection
 **/
@Repository
@Slf4j
public class CbsRepository {

    private final DataSourceConfig dataSourceConfig;


    public CbsRepository(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }

    public List<Map<String, Object>> getAllData() {
        Map<String, String> map = new HashMap<>();
        if (ProductUtils.CBS_ENABLE) {
            try {
                log.info("fetch data from remote server");
                return this.dataSourceConfig.getCbsNamedParameterJdbcTemplate().queryForList(
                    String.format(CbsConstant.QUERY_FOR_LIST, dataSourceConfig.getTableName()),
                    map);
            } catch (Exception e) {
                log.error("unable to fetch data from remote server:::{}", e.getMessage());
                throw new ServiceValidationException("Unable to connect to remote server");
            }
        }
        return new ArrayList<>();
    }
}
