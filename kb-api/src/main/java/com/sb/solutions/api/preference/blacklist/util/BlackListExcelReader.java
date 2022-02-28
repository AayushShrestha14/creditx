package com.sb.solutions.api.preference.blacklist.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.preference.blacklist.entity.BlackList;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.file.FileUploadUtils;

public class BlackListExcelReader {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);
    private static final int NAME_COLUMN_POS = 0;
    private static final int REF_COLUMN_POS = 1;
    private static final int DOCTYPE_COLUMN_POS = 2;

    private BlackListExcelReader() {}

    public static List<BlackList> parseBlackListFile(MultipartFile multipartFile) {
        List<BlackList> customerBlackList = new ArrayList<>();
        Workbook wb = null;
        String excelFileName = multipartFile.getOriginalFilename();

        try {
            if (excelFileName.contains(".xlsx")) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());
            } else {
                wb = new HSSFWorkbook(multipartFile.getInputStream());
            }

            Sheet workSheet = wb.getSheetAt(0);
            IntStream.rangeClosed(1, workSheet.getLastRowNum()).forEach(value -> {
                Row row = workSheet.getRow(value);
                BlackList blackList = new BlackList();

                if (row.getCell(NAME_COLUMN_POS).toString() != null) {
                    blackList.setName(row.getCell(NAME_COLUMN_POS).getStringCellValue());
                }

                if (row.getCell(REF_COLUMN_POS).toString() != null) {
                    /*
                        Determine whether the cell contains numeric or string value.
                    *   If numeric, cast it to string to match the type in BlackList
                    */
                    if (row.getCell(REF_COLUMN_POS).getCellType() == CellType.NUMERIC) {
                        Long refString = (long) row.getCell(REF_COLUMN_POS).getNumericCellValue();
                        blackList.setRef(refString.toString());
                    } else {
                        blackList.setRef(row.getCell(REF_COLUMN_POS).getStringCellValue());
                    }
                }

                if (row.getCell(DOCTYPE_COLUMN_POS).toString() != null) {
                    blackList.setDocType(row.getCell(DOCTYPE_COLUMN_POS).toString().charAt(0));
                }
                customerBlackList.add(blackList);
            });
        } catch (Exception e) {
            logger.error("invalid file format {}", e.getMessage());
            throw new ServiceValidationException("File format is not valid");
        } finally {
            if (null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    logger.error("Failed to close poi workbook {}", e);
                }
            }
        }
        return customerBlackList;
    }
}
