package com.sb.solutions.report.core.factory;

import java.io.FileNotFoundException;

import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.model.Report;
import com.sb.solutions.report.core.report.FormReport;
import com.sb.solutions.report.core.report.SampleReport;
import net.sf.jasperreports.engine.JRException;

/**
 * @author Sunil Babu Shrestha on 4/24/2020
 */
public final class ReportFactory {


    public static Report getReport(ReportParam reportParam) {
        Report report = null;
        switch (reportParam.getReportType()) {
            case FORM_REPORT:
                report = new FormReport(reportParam);
                break;
            default:
                report = new SampleReport(reportParam);
                break;
        }
        try {
            report.generate();
        } catch (JRException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return report;

    }
}
