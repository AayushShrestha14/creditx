package com.sb.solutions.api.nepseCompany.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.sb.solutions.api.nepseCompany.entity.NepseCompany;
import com.sb.solutions.core.enums.ShareType;
import com.sb.solutions.core.exception.ServiceValidationException;
import com.sb.solutions.core.utils.file.FileUploadUtils;

/**
 * @author Sunil Babu Shrestha on 1/17/2020
 * this is utility to read excel load for nepse company
 */
public class NepseExcelReader {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadUtils.class);
    private static final int COMPANY_NAME_COLUMN_POS = 0;
    private static final int SHARE_AMOUNT_COLUMN_POS = 1;
    private static final int COMPANY_CODE_COLUMN_POS = 2;
    private static final int SHARE_TYPE_COLUMN_POS = 3;
    private static final int PRICE_EARNING_RATIO_COLUMN_POS = 4;
    private static final int PRICE_TO_BOOK_VALUE_COLUMN_POS = 5;
    private static final int DIVIDEND_YIELD_COLUMN_POS = 6;
    private static final int DIVIDEND_PAYOUT_RATIO_COLUMN_POS = 7;

    private NepseExcelReader() {
    }

    public static List<NepseCompany> parseNepseCompanyFile(MultipartFile multipartFile) {
        List<NepseCompany> nepseCompanyList = new ArrayList<>();
        Workbook wb = null;
        String excelFileName = multipartFile.getOriginalFilename();

        AtomicReference<Integer> readingRow = new AtomicReference<>(0);
        try {
            if (excelFileName.contains(".xlsx")) {
                wb = new XSSFWorkbook(multipartFile.getInputStream());
            } else {
                wb = new HSSFWorkbook(multipartFile.getInputStream());
            }

            Sheet workSheet = wb.getSheetAt(0);
            IntStream.rangeClosed(1, workSheet.getLastRowNum()).forEach(value -> {
                Row row = workSheet.getRow(value);
                readingRow.set(row.getRowNum());
                NepseCompany nepseCompany = new NepseCompany();

                if (row.getCell(COMPANY_NAME_COLUMN_POS).toString() != null) {
                    nepseCompany
                        .setCompanyName(row.getCell(COMPANY_NAME_COLUMN_POS).getStringCellValue());
                }
                if (row.getCell(SHARE_AMOUNT_COLUMN_POS).toString() != null) {
                    nepseCompany.setAmountPerUnit(
                        row.getCell(SHARE_AMOUNT_COLUMN_POS).getNumericCellValue());
                }
                if (row.getCell(COMPANY_CODE_COLUMN_POS).toString() != null) {
                    nepseCompany.setCompanyCode(row.getCell(COMPANY_CODE_COLUMN_POS).toString());
                }
                if (row.getCell(SHARE_TYPE_COLUMN_POS).toString() != null) {
                    if (!(row.getCell(SHARE_TYPE_COLUMN_POS).toString().trim()
                        .equalsIgnoreCase(ShareType.ORDINARY.toString())
                        || row.getCell(SHARE_TYPE_COLUMN_POS).toString().trim()
                        .equalsIgnoreCase(ShareType.PROMOTER.toString()))) {
                        throw new ServiceValidationException(
                            row.getCell(SHARE_TYPE_COLUMN_POS).toString() + " "
                                + "is  not valid ShareType on row" + " " + (value - 1));
                    }
                    nepseCompany.setShareType(
                        ShareType.valueOf(row.getCell(3).getStringCellValue().toUpperCase()));
                }
                Cell additionalCell = row.getCell(PRICE_EARNING_RATIO_COLUMN_POS);
                if (null != additionalCell && additionalCell.toString() != null) {
                    nepseCompany.setPriceEarningRatio(additionalCell.getNumericCellValue());
                }
                additionalCell = row.getCell(PRICE_TO_BOOK_VALUE_COLUMN_POS);
                if (null != additionalCell && additionalCell.toString() != null) {
                    nepseCompany.setPriceToBookValue(additionalCell.getNumericCellValue());
                }
                additionalCell = row.getCell(DIVIDEND_YIELD_COLUMN_POS);
                if (null != additionalCell && additionalCell.toString() != null) {
                    nepseCompany.setDividendYield(additionalCell.getNumericCellValue());
                }
                additionalCell = row.getCell(DIVIDEND_PAYOUT_RATIO_COLUMN_POS);
                if (null != additionalCell && additionalCell.toString() != null) {
                    nepseCompany.setDividendPayoutRatio(additionalCell.getNumericCellValue());
                }
                nepseCompanyList.add(nepseCompany);
            });
        } catch (Exception e) {
            logger
                .error("Failed Reading Excel Row " + (readingRow.get() + 1) + " " + e.getMessage());
            throw new ServiceValidationException(
                "Failed Reading Excel Row " + (readingRow.get() + 1) + " " + e.getMessage());
        } finally {
            if (null != wb) {
                try {
                    wb.close();
                } catch (IOException e) {
                    logger.error("Failed to close poi workbook {}", e);
                }
            }
        }
        return nepseCompanyList;
    }
}

