package com.sb.solutions.core.constant;

import org.springframework.stereotype.Component;

/**
 * @author Rujan Maharjan on 4/3/2019
 */
@Component
public class FilePath {

    public static String getOSPath() {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return UploadDir.WINDOWS_PATH;
        } else {
            return UploadDir.Linux_PATH;
        }
    }
}
