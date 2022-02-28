package com.sb.solutions.core.jobs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Types;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.GenericStoredProcedure;
import org.springframework.jdbc.object.StoredProcedure;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.utils.file.FileUploadUtils;

/**
 * @author : Rujan Maharjan on  3/16/2020
 **/
@Component
public class BackupDatabase {

    private static final Logger logger = LoggerFactory.getLogger(BackupDatabase.class);

    private static final String ROOT_BACKUP_DIR = FilePath.getOSPath() + "backup";

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;


    @Scheduled(cron = "0 0 20 * * ?")
    public void backup() {
        String procName = "db_backup";
        StoredProcedure storedProcedure = new GenericStoredProcedure();
        storedProcedure.setDataSource(dataSource);
        storedProcedure.setSql(procName);
        storedProcedure.setFunction(false);

        SqlParameter[] sqlParameters = {

            new SqlParameter("filePathName", Types.VARCHAR),
            new SqlParameter("db", Types.VARCHAR),


        };
        Path path = Paths.get(ROOT_BACKUP_DIR);
        if (!Files.exists(path)) {
            new File(ROOT_BACKUP_DIR).mkdirs();
        }
        String fileName = this.databaseName() + System.currentTimeMillis() + ".BAK";
        String filePathName = ROOT_BACKUP_DIR + File.separator + fileName;
        logger.info("backup dir{}", filePathName);
        Map<String, Object> inp = new HashMap<>();
        inp.put("filePathName", filePathName);
        inp.put("db", this.databaseName());
        try {
            storedProcedure.setParameters(sqlParameters);
            storedProcedure.compile();
            storedProcedure.execute(inp);
        } catch (Exception e) {
            logger.error("error while backup database{}", e.getMessage());
        }
    }

    private String databaseName() {
        return this.datasourceUrl.split("=")[1].toLowerCase();
    }

    //zip folder end of the month
    //@Scheduled(fixedRate = 10000)
    private void zipBackup() throws IOException {
        LocalDate localDate = LocalDate.now();
        String currentYearMonth =
            String.valueOf(localDate.getDayOfMonth()) + localDate.getMonth() + localDate.getYear();
        String destination =
            ROOT_BACKUP_DIR + File.separator + this.databaseName() + currentYearMonth + ".zip";
        FileUploadUtils.createZip(ROOT_BACKUP_DIR, destination);
        logger.info("back up database succeed{}", currentYearMonth);
    }

}
