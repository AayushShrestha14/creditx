package com.sb.solutions.core.config.security;

import com.sb.solutions.core.utils.BankUtils;
import com.sb.solutions.core.utils.ProductUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public ProductUtils productModeSetup() {
        return new ProductUtils();
    }

    @Bean
    public BankUtils bankDetailInitialize() {
        return new BankUtils();
    }

}
