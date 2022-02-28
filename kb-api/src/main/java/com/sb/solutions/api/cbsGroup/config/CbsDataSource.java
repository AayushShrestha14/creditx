package com.sb.solutions.api.cbsGroup.config;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : Rujan Maharjan on  12/20/2020
 **/

@Getter
@Setter
@ConfigurationProperties(prefix = "app.datasource")
public class CbsDataSource {

    private Map<String, String> cbs;
    private String tableName;
    private String uniqueKey;

    private ObjectMapper mapper = new ObjectMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);


    public DataSourceProperties cbsDataSourceProperties() {
        return mapper.convertValue(cbs, DataSourceProperties.class);
    }

}
