package com.sb.solutions.core.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "apims")
@Getter
@Setter
public class CbsProperties {

    private String api;
    private String username;
    private String password;
    private String certFilePath;
}
