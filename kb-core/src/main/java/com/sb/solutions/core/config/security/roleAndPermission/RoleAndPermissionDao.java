package com.sb.solutions.core.config.security.roleAndPermission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author Rujan Maharjan on 4/19/2019
 */

@Component
public class RoleAndPermissionDao {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RoleAndPermissionDao.class);

    @Autowired
    DataSource dataSource;

    private String username = "_blank";

    public List<Map<String, Object>> getRole() {
        Map<String, Object> map = new HashMap<>();
        String query =
            "select ua.api_url,"
                + " group_concat(DISTINCT ifnull(r.role_name,'admin')) role_name"
                + " from url_api u"
                + " left join role_permission_rights_api_rights apirights"
                + " on apirights.api_rights_id = ua.id"
                + " left join role_permission_rights rpr"
                + " on rpr.id = apirights.role_permission_rights_id"
                + " left join role r on rpr.role_id = r.id group by ua.id;";

        return namedParameterJdbcTemplate.queryForList(query, map);
    }

    public boolean checkApiPermission(String api) {

        Map<String, Object> map = new HashMap<>();
        map.put("api", api);
        String query = "select api_url from url_api where api_url=:api";
        List<Map<String, Object>> mapApi = namedParameterJdbcTemplate.queryForList(query, map);
        if (mapApi.isEmpty()) {
            return true;
        }
        if (this.getCurrentUserRole().equalsIgnoreCase("admin")) {
            return true;
        }
        List<Map<String, Object>> mapApiChk = this.chkPermissionInRole(api);
        if (!mapApiChk.isEmpty()) {
            return true;
        }

        return false;
    }

    public List<Map<String, Object>> chkPermissionInRole(String api) {
        String role = this.getCurrentUserRole();
        Map<String, Object> map = new HashMap<>();
        map.put("role", role);
        map.put("api", api);
        String query = "select ua.api_url from url_api ua"
            + " left join role_permission_rights_api_rights apirights"
            + " on apirights.api_rights_id = ua.id"
            + " left join role_permission_rights rpr"
            + " on rpr.id= apirights.role_permission_rights_id"
            + " left join role r on rpr.role_id = r.id"
            + " where r.role_name=:role and ua.api_url =:api\n"
            + " group by ua.id;";

        List<Map<String, Object>> mapList = namedParameterJdbcTemplate.queryForList(query, map);
        return mapList;
    }

    public String getCurrentUserRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication
            .getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetail
                = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

            username = userDetail.getUsername();
        } else {
            throw new RuntimeException("Invalid Token");
        }

        Map<String, Object> map = new HashMap<>();
        String query = "SELECT r.role_name from users u join role r on r.id = u.role_id"
            + " where user_name = :username";
        map.put("username", username);

        return namedParameterJdbcTemplate.queryForObject(query, map, String.class);
    }

    public Long getCurrentUserId(String username) {
        Map<String, Object> map = new HashMap<>();
        String query = "SELECT id from users where user_name = :username";
        map.put("username", username);
        Long id = namedParameterJdbcTemplate.queryForObject(query, map, Long.class);
        return id;
    }

    public void restrictUrl(HttpSecurity http) throws Exception {
        List<Map<String, Object>> mapList = this.getRole();
        for (Map<String, Object> map : mapList) {
            if (map.get("api_url") != null) {
                http.authorizeRequests()
                    .antMatchers(map.get("api_url").toString())
                    .hasAnyAuthority(map.get("role_name").toString());
            }
        }
    }

    public List<Map<String, Object>> getEmailConfig() {
        this.dropProcedure();
        this.executeProcQuery();
        Map<String, Object> map1 = new HashMap<>();
        String query = "SELECT username,password,host,port,domain from email_config";

        List<Map<String, Object>> map = namedParameterJdbcTemplate.queryForList(query, map1);
        return map;

    }

    public void executeProcQuery() {
        String query = "CREATE PROCEDURE db_backup \n"
            + "@filePathName nvarchar(100),"
            + "@db nvarchar(100)\n"
            + "AS BEGIN\n"
            + " BACKUP DATABASE @db\n"
            + "TO DISK = @filePathName;\n"
            + " END \n";

        query = query.replaceAll("\\n", " ");
        query = query.replaceAll("\\t", " ");

        try {
            JdbcTemplate tmp = new JdbcTemplate(dataSource);
            tmp.execute(query);
        } catch (Exception e) {
            logger.error("unable to execute create procedure {}", e.getMessage());
        }


    }

    public void dropProcedure() {
        String query = "IF EXISTS (\n"
            + "        SELECT type_desc, type\n"
            + "        FROM sys.procedures WITH(NOLOCK)\n"
            + "        WHERE NAME = 'db_backup'\n"
            + "      )\n"
            + "     DROP PROCEDURE db_backup;\n";

        try {
            JdbcTemplate tmp = new JdbcTemplate(dataSource);
            tmp.execute(query);
        } catch (Exception e) {
            logger.error("unable to execute drop procedure {}", e.getMessage());
        }
    }
}
