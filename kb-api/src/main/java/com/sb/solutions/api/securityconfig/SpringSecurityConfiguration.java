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
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.csrf().disable()
                .authorizeRequests();
        expressionInterceptUrlRegistry = expressionInterceptUrlRegistry.antMatchers("/v1/admin/**")
                .permitAll();
        expressionInterceptUrlRegistry = setUpPermissions(expressionInterceptUrlRegistry);
        expressionInterceptUrlRegistry.antMatchers("/oauth/token")
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
                .antMatchers(HttpMethod.POST, "/v1/customer-otp/regeanerate").permitAll()
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

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry setUpPermissions(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry
                .antMatchers(HttpMethod.POST, "/v1/admin/email-config")
                .hasAuthority("email_config_update")
                .antMatchers(HttpMethod.GET, "/v1/admin/email-config/all")
                .hasAuthority("email_config_list")
                .antMatchers(HttpMethod.POST, "/v1/admin/email-config/test")
                .hasAuthority("email_config_test")
                .antMatchers(HttpMethod.POST, "/v1/admin/base-interest/list")
                .hasAuthority("base_interest_list")
                .antMatchers(HttpMethod.GET, "/v1/admin/base-interest/all")
                .hasAuthority("base_interest_all")
                .antMatchers(HttpMethod.GET, "/v1/admin/base-interest/{id}")
                .hasAuthority("base_interest_id")
                .antMatchers(HttpMethod.PUT, "/v1/admin/base-interest/{id}")
                .hasAuthority("base_interest_update")
                .antMatchers(HttpMethod.POST, "/v1/admin/base-interest")
                .hasAuthority("base_interest_save")
                .antMatchers(HttpMethod.POST, "/v1/admin/base-interest/active")
                .hasAuthority("base_interest_active")
                .antMatchers(HttpMethod.POST, "/v1/admin/nepse-company")
                .hasAuthority("nepse-company_save")
                .antMatchers(HttpMethod.POST, "/v1/admin/nepse-company/list")
                .hasAuthority("nepse-company_list")
                .antMatchers(HttpMethod.POST, "/v1/admin/nepse-company/uploadNepseFile")
                .hasAuthority("nepse-company_file")
                .antMatchers(HttpMethod.GET, "/v1/admin/nepse-company/statusCount")
                .hasAuthority("nepse-company_count")
                .antMatchers(HttpMethod.POST, "/v1/admin/nepse-company/share")
                .hasAuthority("nepse-company-share-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/nepse-company/share")
                .hasAuthority("nepse-company-share-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/nepse-company/share/list")
                .hasAuthority("nepse-company-share-list")
                .antMatchers(HttpMethod.GET, "/v1/admin/nepse-company/share/{id}")
                .hasAuthority("nepse-company-share-id")
                .antMatchers(HttpMethod.GET, "/v1/admin/blacklist/all")
                .hasAuthority("blacklist_list-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/blacklist/list")
                .hasAuthority("blacklist_list-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/blacklist/uploadBlackList")
                .hasAuthority("blacklist_list-file")
                .antMatchers(HttpMethod.POST, "/v1/admin/notification-master")
                .hasAuthority("notification-master-save")
                .antMatchers(HttpMethod.POST, "/v1/admin/notification-master/one")
                .hasAuthority("notification-maseter-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/notification-master/all")
                .hasAuthority("notification-master-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/notification-master/status")
                .hasAuthority("notification-master-status")
                .antMatchers(HttpMethod.POST, "/v1/admin/fiscal-year")
                .hasAuthority("fiscal-year-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/fiscal-year/all")
                .hasAuthority("fiscal-year-list")
                .antMatchers(HttpMethod.GET, "/v1/admin/fiscal-year/{id}")
                .hasAuthority("fiscal-year-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/fiscal-year/list")
                .hasAuthority("fiscal-year-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/approval-limit")
                .hasAuthority("approval-limit-save")
                .antMatchers(HttpMethod.POST, "/v1/admin/approval-limit/list")
                .hasAuthority("approval-limit-list")
                .antMatchers(HttpMethod.GET, "/v1/admin/approval-limit/{id}/{loanCategory}/role")
                .hasAuthority("approval-limit-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/role")
                .hasAuthority("role-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/all")
                .hasAuthority("role-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/role/edit")
                .hasAuthority("role-update")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/{id}")
                .hasAuthority("role-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/statusCount")
                .hasAuthority("role-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/active")
                .hasAuthority("role-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/maker")
                .hasAuthority("role-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/role/getApproval")
                .hasAuthority("role-save")
                .antMatchers(HttpMethod.POST, "/v1/admin/roleRightPermission")
                .hasAuthority("role-right-permission-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/roleRightPermission/{id}")
                .hasAuthority("role-right-permission-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/roleRightPermission/rights")
                .hasAuthority("role-right-permission-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/permission")
                .hasAuthority("permission-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/permission/all")
                .hasAuthority("permission-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/permission/chkPerm")
                .hasAuthority("permission-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/role-hierarchy")
                .hasAuthority("role-hierarchy-save")
                .antMatchers(HttpMethod.GET, "/v1/admin/role-hierarchy/all")
                .hasAuthority("role-hierarchy-list")
                .antMatchers(HttpMethod.GET, "/v1/admin/role-hierarchy/getForward")
                .hasAuthority("role-hierarchy-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/role-hierarchy/getBackward")
                .hasAuthority("role-hierarchy-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/loan-configs")
                .hasAuthority("loan-configs-save")
                .antMatchers(HttpMethod.POST, "/v1/admin/loan-configs/list")
                .hasAuthority("loan-configs-list")
                .antMatchers(HttpMethod.POST, "/v1/admin/loan-configs/statusCount")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/loan-configs/statusCount")
                .hasAuthority("loan-configs-list")
                .antMatchers(HttpMethod.GET, "/v1/admin/loan-configs/{id}")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.POST, "/v1/admin/loan-configs/status")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/loan-configs/all/eligibility")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/loan-configs/{loanConfigId}/eligibility")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.GET, "/v1/admin/loan-configs/{loanCategory}/all")
                .hasAuthority("loan-configs-get")
                .antMatchers(HttpMethod.POST,"/v1/admin/accountType")
                .hasAuthority("account-type-save")
                .antMatchers(HttpMethod.POST,"/v1/admin/accountType/list")
                .hasAuthority("account-type-list")
                .antMatchers(HttpMethod.GET,"/v1/admin/accountType/{id}")
                .hasAuthority("account-type-get")
                .antMatchers(HttpMethod.GET,"/v1/admin/accountType/all")
                .hasAuthority("account-type-list")
                .antMatchers(HttpMethod.PUT,"/v1/admin/accountType/{id}")
                .hasAuthority("account-type-update")
                .antMatchers(HttpMethod.POST,"/v1/admin/accountCategory")
                .hasAuthority("account-category-save")
                .antMatchers(HttpMethod.POST,"/v1/admin/accountCategory/list")
                .hasAuthority("account-category-list")
                .antMatchers(HttpMethod.GET,"/v1/admin/accountCategory/all")
                .hasAuthority("account-category-list")
                .antMatchers(HttpMethod.GET,"/v1/admin/accountCategory/accountType/{accountTypeId}")
                .hasAuthority("account-category-get")
                .antMatchers(HttpMethod.PUT,"/v1/admin/accountCategory/{id}")
                .hasAuthority("account-category-get")

                ;
        return expressionInterceptUrlRegistry;
    }


}
