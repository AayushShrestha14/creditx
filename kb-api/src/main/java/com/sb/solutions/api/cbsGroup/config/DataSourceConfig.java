package com.sb.solutions.api.cbsGroup.config;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

/**
 * @author : Rujan Maharjan on  12/20/2020
 **/
@Component
public class DataSourceConfig {

    private final CbsDataSource cbsDataSource;

    public DataSourceConfig(
        CbsDataSource cbsDataSource) {
        this.cbsDataSource = cbsDataSource;
    }


    public DataSource dataSourceCsb() {
        return cbsDataSource.cbsDataSourceProperties().initializeDataSourceBuilder()
            .type(HikariDataSource.class).build();
    }

    public String getTableName() {
        return cbsDataSource.getTableName();
    }

    public String getUniqueKeyForFilter() {
        return cbsDataSource.getUniqueKey();
    }

    public NamedParameterJdbcTemplate getCbsNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSourceCsb());
    }

}
