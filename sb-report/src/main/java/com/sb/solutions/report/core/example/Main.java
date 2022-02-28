package com.sb.solutions.report.core.example;

import java.util.Arrays;
import java.util.List;

import ar.com.fdvs.dj.domain.DJCalculation;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.GroupBuilder;
import ar.com.fdvs.dj.domain.constants.GroupLayout;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.domain.entities.columns.PropertyColumn;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.ReportType;
import com.sb.solutions.report.core.factory.ReportFactory;
import com.sb.solutions.report.core.util.StyleUtil;

/**
 * @author Sunil Babu Shrestha on 4/24/2020
 */
public class Main {

    public static void main(String[] args) {
        //Create more columns
        AbstractColumn columnBranch = ColumnBuilder.getNew()
            .setColumnProperty("branch", String.class.getName())
            .setTitle("Branch").setWidth(85)
            .build();

        AbstractColumn columnaProductLine = ColumnBuilder.getNew()
            .setColumnProperty("productLine", String.class.getName())
            .setTitle("Product Line").setWidth(85)
            .build();

        AbstractColumn columnaItem = ColumnBuilder.getNew()
            .setColumnProperty("item", String.class.getName())
            .setTitle("Item").setWidth(85)
            .build();

        AbstractColumn columnQuantity = ColumnBuilder.getNew()
            .setColumnProperty("quantity", Double.class.getName())
            .setTitle("Quantity").setWidth(80)
            .build();

        AbstractColumn columnAmount = ColumnBuilder.getNew()
            .setColumnProperty("amount", Double.class.getName())
            .setTitle("Amount").setWidth(90)
            .setPattern(
                "$ 0.00")        //defines a pattern to apply to the values swhown (uses TextFormat)
            .setStyle(StyleUtil.numberStyle())// special style for this column (align right)
            .build();

        GroupBuilder gb = new GroupBuilder(); // Create another group (using another column as criteria)
        DJGroup amountGroup = gb.setCriteriaColumn(
            (PropertyColumn) columnBranch) // and we add the same operations for the columnQuantity and
            .addFooterVariable(columnAmount, DJCalculation.SUM)
            .setGroupLayout(GroupLayout.VALUE_IN_HEADER)
            .build();

        List<AbstractColumn> columnList = Arrays
            .asList(columnBranch, columnaProductLine, columnaItem, columnQuantity, columnAmount);

//        ReportParam reportParam = new ReportParam("Test Title", "Test SubTitle", columnList,
//            Arrays.asList(getDummy(), getDummy()));
        ReportParam reportParam1 = ReportParam.builder().title("My First Report Title")
            .subTitle("Test SubTitle").columns(columnList)
            .data(Arrays.asList(getDummy(), getDummy())).reportType(ReportType.SAMPLE_REPORT)
            .djGroups(Arrays.asList(amountGroup)).exportType(ExportType.XLS)
            .build();

        ReportFactory.getReport(reportParam1);
    }


    private static TestReportData getDummy() {
        TestReportData data = new TestReportData();
        data.setAmount(10d);
        data.setBranch("kathmandu");
        data.setItem("biscuit");
        data.setProductLine("parsa plant");
        data.setQuantity(100d);
        return data;
    }
}
