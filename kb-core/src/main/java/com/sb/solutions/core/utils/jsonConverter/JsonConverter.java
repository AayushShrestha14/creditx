package com.sb.solutions.core.utils.jsonConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sb.solutions.core.constant.FilePath;
import com.sb.solutions.core.exception.ServiceValidationException;

public class JsonConverter {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);
    private final Gson gson = new Gson();

    public String convertToJson(Object object) {
        return gson.toJson(object);
    }

    public <T> T convertToJson(String path, Class<T> clazz) {
        final StringBuilder jsonCustomerData = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonCustomerData.append(line);
            }
            return gson.fromJson(jsonCustomerData.toString(), clazz);
        } catch (Exception e) {
            throw new ServiceValidationException(e.toString());
        }
    }

    public Object readJsonFile(String url) {
        org.json.simple.parser.JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(FilePath.getOSPath() + url);
            try {
                return parser.parse(reader);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public String writeJsonFile(String url, String jsonFileName, Object data) {

        Path path = Paths.get(FilePath.getOSPath() + url);
        if (!Files.exists(path)) {
            new File(FilePath.getOSPath() + url).mkdirs();
        }
        File file = new File(FilePath.getOSPath() + jsonFileName);
        file.getParentFile().mkdirs();
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);

            writer.write(this.convertToJson(data));
            return jsonFileName;
        } catch (IOException e) {
            logger.error("Error occurred {}", e);
        } finally {
            try {
                writer.flush();

            } catch (IOException e) {
                logger.error("Error occurred {}", e);
            }
        }

        return null;
    }

    public String updateJsonFile(String url, Object data) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(FilePath.getOSPath() + url);

            writer.write(this.convertToJson(data));

            return url;

        } catch (IOException e) {
            logger.error("Error occurred {}", e);
        } finally {
            try {
                writer.flush();
            } catch (IOException e) {
                logger.error("Error occurred {}", e);
            }

        }
        return null;
    }
}
