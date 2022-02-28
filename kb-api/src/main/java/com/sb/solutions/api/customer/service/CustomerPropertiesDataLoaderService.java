package com.sb.solutions.api.customer.service;

import java.util.Map;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Sunil Babu Shrestha on 11/12/2020
 */

@Component
@PropertySource(value = "classpath:data/customer.properties")
@EnableConfigurationProperties
public class CustomerPropertiesDataLoaderService {

    @Value("#{${subsector}}")
    @Getter
    private Map<String, String> subSectors;

}
