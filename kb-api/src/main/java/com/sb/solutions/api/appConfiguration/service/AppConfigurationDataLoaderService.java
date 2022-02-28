package com.sb.solutions.api.appConfiguration.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sb.solutions.core.utils.ProductUtils;

/**
 * @author : Rujan Maharjan on  1/6/2021
 **/
@Service
public class AppConfigurationDataLoaderService {

    private final AppConfigPropertiesDataLoaderService appConfigPropertiesDataLoaderService;

    public AppConfigurationDataLoaderService(
        AppConfigPropertiesDataLoaderService appConfigPropertiesDataLoaderService) {
        this.appConfigPropertiesDataLoaderService = appConfigPropertiesDataLoaderService;
    }

    public List<String> getRoleType() {
        if (ProductUtils.FULL_CAD) {
            return Arrays.asList(appConfigPropertiesDataLoaderService.getRoleTypeListFullCAD().clone());
        }else{
            return Arrays.asList(appConfigPropertiesDataLoaderService.getRoleTypeDefault().clone());
        }
    }
}
