package com.sb.solutions.core.utils.base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

/**
 * Created by Rujan Maharjan on 2/25/2019.
 */

@Component
public class Base64Convertor {

    public String encode(String filePath) {

        File file = new File(filePath);
        byte[] bytes = new byte[0];
        try {
            bytes = loadFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] encoded = Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);
        String extension = Utility.getFileExtension(file.getName());
        String imageType = "data:image/" + extension + ";base64,";
        return imageType + encodedString;
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
            && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
