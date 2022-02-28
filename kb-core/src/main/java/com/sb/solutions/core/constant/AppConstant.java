package com.sb.solutions.core.constant;

import lombok.experimental.UtilityClass;

/**
 * @author Sunil Babu Shrestha on 9/22/2019
 */
@UtilityClass
public class AppConstant {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String MM_DD_YYYY = "MM-dd-yyyy";
    // List available template id
    public static final int TEMPLATE_SHARE_SECURITY = 13;
    public static final int TEMPLATE_INSURANCE = 17;

    // Role Constant
    public static final String ADMIN_ROLE = "admin";

    // Permission Constant
    public static final int ACCOUNT_OPENING_PERMISSION = 19;
    public static final int ELIGIBILITY_PERMISSION = 18;
    public static final int ELIGIBILITY_PERMISSION_SUBNAV_QUESTION = 4;
    public static final int ELIGIBILITY_PERMISSION_SUBNAV_GENERAL_QUESTION = 5;

    public static final String SEPERATOR_FRONT_SLASH = "/";
    public static final String SEPERATOR_BLANK = " ";
}
