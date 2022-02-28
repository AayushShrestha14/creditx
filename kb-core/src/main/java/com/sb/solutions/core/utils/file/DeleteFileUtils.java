package com.sb.solutions.core.utils.file;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import com.sb.solutions.core.constant.FilePath;

/**
 * @author : Rujan Maharjan on  5/6/2021
 **/

@Slf4j
public class DeleteFileUtils {


    public static void deleteFile(String relativePath) {
        String fullPath = FilePath.getOSPath().concat(relativePath);
        new Thread(() -> {
            File myObj = new File(fullPath);
            if (myObj.delete()) {
                log.info("Successfully delete file {}", fullPath);
            } else {
                log.info("Failed to delete the file.{}", fullPath);
            }
        }).start();

    }
}
