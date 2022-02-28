package com.sb.solutions.service.report;

import java.util.Map;

import com.sb.solutions.report.core.service.FormReportGeneratorService;

/**
 * @author : Rujan Maharjan on  5/19/2021
 **/
public interface CadReportService extends FormReportGeneratorService {

    String reportPath(Map<String,String> filterParams);

}
