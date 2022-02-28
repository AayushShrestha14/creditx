package com.sb.solutions.core.constant;

/**
 * @author Rujan Maharjan on 7/10/2019
 */
public final class BaseConfigurationPatchUtils {

    private static final String MSSQL_PATCH_LOCATION = "/patch_ms_sql";
    private static final String MYSQL_PATCH_LOCATION = "/patch_my_sql";
    private static final String ORACLE_PATCH_LOCATION = "/patch_oracle";
    private static final String POSTGRES_PATCH_LOCATION = "/patch_postgres";

    private BaseConfigurationPatchUtils() {
    }

    public static String currentConnectedDb(String dbUrl) throws Exception {
        if (dbUrl.contains("mysql")) {
            return BaseConfigurationPatchUtils.MYSQL_PATCH_LOCATION;
        } else if (dbUrl.contains("sqlserver")) {
            return BaseConfigurationPatchUtils.MSSQL_PATCH_LOCATION;
        } else if (dbUrl.contains("oracle")) {
            return BaseConfigurationPatchUtils.ORACLE_PATCH_LOCATION;
        } else if (dbUrl.contains("postgres")) {
            return BaseConfigurationPatchUtils.POSTGRES_PATCH_LOCATION;
        } else {
            throw new Exception("Invalid Database Url " + dbUrl);
        }

    }
}
