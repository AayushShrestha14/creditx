package com.sb.solutions.core.config.security.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file", ignoreUnknownFields = false)
@Data
public class FileStorageProperties {

    private String uploadDirectory;

}
