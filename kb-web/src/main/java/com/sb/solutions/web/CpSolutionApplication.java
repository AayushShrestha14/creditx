package com.sb.solutions.web;

import com.sb.solutions.api.basehttp.BaseHttp;
import com.sb.solutions.api.basehttp.BaseHttpRepo;
import com.sb.solutions.api.cbsGroup.config.CbsDataSource;
import com.sb.solutions.api.user.repository.UserRepository;
import com.sb.solutions.core.config.security.SpringSecurityAuditorAware;
import com.sb.solutions.core.config.security.property.FileStorageProperties;
import com.sb.solutions.core.config.security.property.MailProperties;
import com.sb.solutions.core.constant.BaseConfigurationPatchUtils;
import com.sb.solutions.core.utils.BankUtils;
import com.sb.solutions.core.utils.ProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @author Rujan Maharjan on 12/27/2018
 */
@SpringBootApplication(scanBasePackages = "com.sb.solutions")
@ComponentScan(basePackages = "com.sb.solutions")
@EnableJpaRepositories(basePackages = "com.sb.solutions")
@EntityScan(basePackages = "com.sb.solutions")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
@EnableConfigurationProperties({FileStorageProperties.class, MailProperties.class,
        CbsDataSource.class})
public class CpSolutionApplication extends SpringBootServletInitializer {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BaseHttpRepo baseHttpRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;


    @Autowired
    private final ProductUtils productUtils;

    @Autowired
    private final BankUtils bankUtils;

    @Value("${server.port}")
    private String port;

    @Value("${bank.affiliateId}")
    private String affiliateId;

    @Value("${spring.datasource.url}")
    private String dbValue;

    public CpSolutionApplication(ProductUtils productUtils, BankUtils bankUtils) {
        this.productUtils = productUtils;
        this.bankUtils = bankUtils;
    }


    public static void main(String[] args) {
        SpringApplication.run(CpSolutionApplication.class, args);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(10);
        t.setMaxPoolSize(100);
        t.setQueueCapacity(50);
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(120);
        return t;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CpSolutionApplication.class);
    }

    @PostConstruct
    public void initialize() {
        try {
            String baseServerFolder = BaseConfigurationPatchUtils.currentConnectedDb(dbValue);
            InitialPatch.inital(baseServerFolder, dataSource);
            if (baseHttpRepo.findAll().isEmpty()) {
                BaseHttp baseHttp = new BaseHttp();
                baseHttp.setBaseUrl("http://" + baseHttp.getHostAddress() + ":" + port + "/");
                baseHttp.setFlag(1);
                baseHttpRepo.save(baseHttp);
            }
            ClassPathResource dataResourc = new ClassPathResource(
                    baseServerFolder + "/loan_sql/patch_product_mode.sql");
            ResourceDatabasePopulator populators = new ResourceDatabasePopulator(dataResourc);
            populators.execute(dataSource);
            ClassPathResource dataResourceGeneral = new ClassPathResource(
                    baseServerFolder + "/loan_sql/patch_general_permission.sql");
            ResourceDatabasePopulator populatorGeneral = new ResourceDatabasePopulator(
                    dataResourceGeneral);
            populatorGeneral.execute(dataSource);

            if (ProductUtils.ACCOUNT) {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_account_opening.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            } else {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_remove_account_opening.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            }

            if (ProductUtils.ELIGIBILITY) {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_eligibility_permission.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            } else {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_remove_eligibility.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            }

            if (ProductUtils.MEMO) {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_memo.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            } else {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_remove_memo.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            }

            if (ProductUtils.OFFER_LETTER) {
                if (affiliateId.equals("mega")) {
                    ClassPathResource dataResource = new ClassPathResource(
                            baseServerFolder + "/general_patch/offer_letter/" + affiliateId + ".sql");
                    ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                            dataResource);
                    populator.execute(dataSource);
                }
                ClassPathResource cadResource = new ClassPathResource(
                        baseServerFolder + "/general_patch/role_cad.sql");
                ResourceDatabasePopulator cadPopulator = new ResourceDatabasePopulator(
                        cadResource);
                cadPopulator.execute(dataSource);
            }

            if (affiliateId.equals("mega")) {
                ClassPathResource dataResourceCheckListDoc = new ClassPathResource(
                        baseServerFolder + "/general_patch/cad_template_checkList_doc.sql");
                ResourceDatabasePopulator cadCheckListTemplateDocument = new ResourceDatabasePopulator(
                        dataResourceCheckListDoc);

                cadCheckListTemplateDocument.execute(dataSource);
            }

            if (ProductUtils.LAS) {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/patch_las_permission.sql");
                ClassPathResource dataResourceTemplate = new ClassPathResource(
                        baseServerFolder + "/loan_sql/las_loan_template.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource, dataResourceTemplate);
                populator.execute(dataSource);
            }
            if (ProductUtils.FULL_CAD) {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/cad/cad-menu.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            } else {
                ClassPathResource dataResource = new ClassPathResource(
                        baseServerFolder + "/loan_sql/cad/remove-cad-menu.sql");
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        dataResource);
                populator.execute(dataSource);
            }

            this.permissionRemoveForDMSandLAS(ProductUtils.DMS, ProductUtils.LAS,
                    baseServerFolder);
            this.permissionRemoveForDMSandLAS(ProductUtils.DMS, ProductUtils.LAS,
                    baseServerFolder);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("cannot load patch file");
        }

    }


    private void permissionRemoveForDMSandLAS(Boolean dms, Boolean las,
                                              String baseServerFolder) {
        if (dms && !las) {
            ClassPathResource dataResource = new ClassPathResource(
                    baseServerFolder + "/loan_sql/patch_remove_only_las.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataResource);
            populator.execute(dataSource);
        }
        if (!dms && !las) {
            ClassPathResource dataResource = new ClassPathResource(
                    baseServerFolder + "/loan_sql/patch_remove_dms_las_permission.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataResource);
            populator.execute(dataSource);
        }
    }

    @Bean
    public AuditorAware<Long> auditorAware() {
        return new SpringSecurityAuditorAware();
    }



    @RequestMapping("/")
    public String errorPage() {
        return "error";
    }
}
