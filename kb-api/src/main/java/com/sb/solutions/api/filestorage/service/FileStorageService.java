package com.sb.solutions.api.filestorage.service;

import org.springframework.core.io.Resource;

public interface FileStorageService {

    String storeDocument(String encodedImage, String imageName, String destinationPath);

    String getFilePath(String... paths);

    String encodeFileUrl(String filePath);

    Resource getFile(String encodedHash, String fileName);

}
