package com.sb.solutions.report.core.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.sb.solutions.report.core.bean.ReportParam;

/**
 * @author Sunil Babu Shrestha on 4/5/2020
 */

public interface FormReportGeneratorService {

    String title();

    default String subTitle() {
        return "This report was generated on " + LocalDate.now();
    }

    List<AbstractColumn> columns();

    ReportParam populate(Optional optional);


}
