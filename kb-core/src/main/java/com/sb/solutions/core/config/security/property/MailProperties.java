package com.sb.solutions.core.config.security.property;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "mailproperty")
public class MailProperties {

    private Map<String, String> additionalHeaders;

}
