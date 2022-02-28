package com.sb.solutions.core.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Rujan Maharjan on 4/3/2019
 */
@Component
public class UploadDir {

    public static String WINDOWS_PATH;
    public static String Linux_PATH = "/var/";
    public static String branchCsv = "images/branchCsv";
    public static String userCsv = "images/userCsv";
    public static String customerLoanCsv = "images/customerLoanCsv";
    public static String userProfile = "images/userProfile/";
    public static String userSignature = "images/userSignature/";
    public static String initialDocument = "images/";

    @Value("${file.upload-directory}")
    public void setWindowsPath(String windowsPath) {
        WINDOWS_PATH = windowsPath;
    }
}
