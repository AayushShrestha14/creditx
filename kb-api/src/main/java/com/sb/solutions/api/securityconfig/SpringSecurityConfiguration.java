package com.sb.solutions.api.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author : Rujan Maharjan on  6/12/2021
 **/
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bcryptEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/oauth/token")
                .permitAll()
                .antMatchers("/v1/users/register")
                .permitAll()
                .antMatchers("/v1/user/test")
                .permitAll()
                .antMatchers("/v1/user/mail")
                .permitAll()
                .antMatchers("/v1/user/resetPassword")
                .permitAll()
                .antMatchers("/v1/user/forgotPassword").permitAll()
                .antMatchers("/v1/branch/limited").permitAll()
                .antMatchers("/v1/address/province").permitAll()
                .antMatchers("/v1/address/districtByProvince").permitAll()
                .antMatchers("/v1/address/municipalityVdcByDistrict").permitAll()
                .antMatchers("/v1/address/district").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/customer-otp").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/customer-otp/verify").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/customer-otp/regenerate").permitAll()
                .antMatchers("/v1/accountType/all").permitAll()
                .antMatchers("/v1/accountCategory/all").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/accountCategory/accountType/*").permitAll()
                .antMatchers("/v1/accountOpening").permitAll()
                .antMatchers("/v1/accountOpening/uploadFile").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/loan-configs/all/eligibility").permitAll()
                .antMatchers("/v1/loan-configs/*/eligibility").permitAll()
                .antMatchers("/v1/loan-configs/*/applicants").permitAll()
                .antMatchers("/v1/loan-configs/*/questions").permitAll()
                .antMatchers("/v1/loan-configs/*/applicants/*/documents").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/eligibility-criterias").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/calendar").permitAll()
                .antMatchers("/actuator/**").authenticated()
                .antMatchers("/swagger-ui.html").authenticated()
                .antMatchers("/v1/**")
                .authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/oauth/logout"))
                .logoutSuccessUrl("/api/language")
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }



}
