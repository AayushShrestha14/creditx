package com.sb.solutions.core.utils.csv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rujan Maharjan on 6/7/2019
 */

@Component
public class CsvReader {


    public List<Map<String, Object>> excelReader(MultipartFile filepath) {
        List<Map<String, Object>> excelToList = new ArrayList<>();
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(filepath.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet worksheet = workbook.getSheetAt(0);

        for (int k = 1; k < worksheet.getPhysicalNumberOfRows(); k++) {
            Map<String, Object> maps = new LinkedHashMap<>();
            HSSFRow row = worksheet.getRow(k);
            for (int j = 0; j < row.getSheet().getRow(k).getLastCellNum(); j++) {
                maps.put(row.getSheet().getRow(0).getCell(j).toString(),
                    row.getSheet().getRow(k).getCell(j).toString());
            }
            excelToList.add(maps);
        }
        return excelToList;
    }
}
