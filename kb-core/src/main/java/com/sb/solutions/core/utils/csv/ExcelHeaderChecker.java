package com.sb.solutions.core.utils.csv;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Row;

import com.sb.solutions.core.exception.ServiceValidationException;



public class ExcelHeaderChecker {
    public void checkNepseHeader(Row row) {
        row.forEach(c -> {
            if (!(Arrays.asList("companyname", "value", "companycode", "sharetype")
                    .contains(c.getStringCellValue().replaceAll("\\s", "").toLowerCase()))) {
                throw new ServiceValidationException(c.getStringCellValue() + " " + "is not valid Header");
            }
        });
    }

}
