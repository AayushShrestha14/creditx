package com.sb.solutions.api.filestorage.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileCopyUtils;

import com.sb.solutions.api.filestorage.service.FileStorageService;
import com.sb.solutions.core.config.security.property.FileStorageProperties;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final FileStorageProperties fileStorageProperties;

    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    @Override
    public String storeDocument(String encodedImage, String imageName, String destinationPath) {
        logger.debug("Storing the base 64 encoded image to destination location.");
        makeDirectory(destinationPath);
        File destination = new File(destinationPath + File.separator + imageName);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        try {
            FileCopyUtils.copy(decodedBytes, destination);
            return encodeFileUrl(destination.getPath());
        } catch (IOException e) {
            logger.debug("Unable to write image to file system due to [{}].", e.getMessage());
            return null;
        }
    }

    @Override
    public String getFilePath(String... paths) {
        logger.debug("Generating the file path.");
        String filePath = fileStorageProperties.getUploadDirectory();
        for (String path : paths) {
            filePath += File.separator + path;
        }
        return filePath;
    }

    @Override
    public String encodeFileUrl(String filePath) {
        String sourcePath = filePath.substring(fileStorageProperties.getUploadDirectory().length());
        String imageName = sourcePath.substring(sourcePath.lastIndexOf(File.separator) + 1);
        sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf(File.separator));
        sourcePath = sourcePath.replace(File.separatorChar, '_');
        try {
            return Base64Utils.encodeToUrlSafeString(sourcePath.getBytes())
                + "/" + URLEncoder.encode(imageName, "UTF-8");
        } catch (IOException e) {
            logger.debug("Encoding failed due to [{}].", e.getMessage());
        }
        return null;
    }

    @Override
    public Resource getFile(String encodedHash, String fileName) {
        logger.debug("Getting the file as resource.");
        String decodedPath = new String(Base64Utils.decodeFromUrlSafeString(encodedHash),
            Charset.forName("UTF-8"));
        String filePath = getFilePath(decodedPath.split("_")) + File.separator + fileName;
        File image = new File(filePath);
        if (image.exists()) {
            try {
                Resource resource = new UrlResource(image.toURI());
                return resource;
            } catch (MalformedURLException e) {
                logger.debug("Not able to retrieve the resource due to [{}].", e.getMessage());
            }
        }
        return null;
    }

    private void makeDirectory(String directoryPath) {
        File filePath = new File(directoryPath);
        filePath.mkdirs();
    }
}
