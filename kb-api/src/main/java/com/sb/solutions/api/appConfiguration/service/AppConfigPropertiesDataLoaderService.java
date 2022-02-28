package com.sb.solutions.api.appConfiguration.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author : Rujan Maharjan on  1/6/2021
 **/

@Component
@PropertySource(value = "classpath:data/appConfig.properties")
@EnableConfigurationProperties
public class AppConfigPropertiesDataLoaderService {

    @Value("#{'${roleTypeDefault}'.split(',')}")
    @Getter
    private String[] roleTypeDefault;

    @Value("${roleTypeFullCad}")
    @Getter
    private String[] roleTypeListFullCAD;

}
