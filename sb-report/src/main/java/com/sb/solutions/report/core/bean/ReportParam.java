package com.sb.solutions.report.core.bean;

import java.util.List;

import lombok.Builder;
import lombok.Data;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.enums.FormType;
import com.sb.solutions.report.core.enums.ReportType;

/**
 * @author Sunil Babu Shrestha on 4/5/2020
 */

@Builder
@Data
public class ReportParam {

    private static final int HEIGHT = 15;
    private static final int MARGIN_SIDE = 30;
    private static final int MARGIN_TOP = 20;
    private static final int MARGIN_BOTTOM = 15;
    //To set the Excel sheet name (one sheet only)
    private final String reportName;
    private final ReportType reportType;
    private final String title;
    private final String subTitle;
    private final List<AbstractColumn> columns;
    private final List<Object> data;
    private final List<DJGroup> djGroups;
    private int height = HEIGHT;
    private int leftMargin = MARGIN_SIDE;
    private int topMargin = MARGIN_TOP;
    private int rightMargin = MARGIN_SIDE;
    private int bottomMargin = MARGIN_BOTTOM;
    private Style titleStyle;
    private Style subTitleStyle;
    private ExportType exportType;
    private FormType formType;
    private String filePath;


}
