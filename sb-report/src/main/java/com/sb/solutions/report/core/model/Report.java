package com.sb.solutions.report.core.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.ImageBanner;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.entities.DJGroup;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import ar.com.fdvs.dj.util.SortUtils;
import com.sb.solutions.report.core.bean.ReportParam;
import com.sb.solutions.report.core.enums.ExportType;
import com.sb.solutions.report.core.util.ReportExporter;
import com.sb.solutions.report.core.util.StyleUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * @author Sunil Babu Shrestha on 4/24/2020
 */
@RequiredArgsConstructor
public abstract class Report {

    private final ReportParam reportParam;
    private String fileName;

    private DynamicReport buildReport() {
        // Report Builder Initialization
        DynamicReportBuilder drb = new FastReportBuilder();
        drb.setReportName(reportParam.getReportName());
        drb.setTitleStyle(StyleUtil.titleStyle());
        drb.setTitle(reportParam.getTitle());
        drb.setSubtitle(reportParam.getSubTitle());
        drb.setDetailHeight(
            reportParam.getHeight());     //defines the height for each record of the report
        drb.setMargins(reportParam.getTopMargin(),
            reportParam.getBottomMargin(),
            reportParam.getLeftMargin(),
            reportParam.getRightMargin());//define the margin space for each side
        drb.setDefaultStyles(StyleUtil.titleStyle(), null, StyleUtil.headerStyle(),
            StyleUtil.detailStyle());
//        drb.addImageBanner(getAbsolutePathForLogoImage(), new Integer(100), new Integer(100),
//            ImageBanner.Alignment.Right)
//            .setOddRowBackgroundStyle(oddRowStyle());

        drb.addFirstPageImageBanner(getAbsolutePathForLogoImage(), new Integer(100),
            new Integer(100),
            ImageBanner.Alignment.Right);

        /**
         * We add the columns to the report (through the builder) in the order
         * we want them to appear
         */

        for (AbstractColumn ac : reportParam.getColumns()) {
            drb.addColumn(ac);


        }

        /**
         *  Group Criteria
         */

        if (null != reportParam.getDjGroups()) {
            for (DJGroup group : reportParam.getDjGroups()) {
                drb.addGroup(group);
            }
        }

        /**
         * add some more options to the report (through the builder)
         */
        drb.setUseFullPageWidth(
            false);  //we tell the report to use the full width of the page. this rezises
        //the columns width proportionally to meat the page width.
        drb.setIgnorePagination(true);
        DynamicReport dr = drb.build(); //Finally build the report!
        return dr;
    }

    public Report generate() throws JRException, FileNotFoundException {

        DynamicReport report = buildReport();
        JRDataSource ds = getDataSource(report);  //Create a JRDataSource, the Collection used

        //Creates the JasperPrint object, we pass as a Parameter
        //the DynamicReport, a new ClassicLayoutManager instance (this
        //one does the magic) and the JRDataSource

        JasperPrint jp = DynamicJasperHelper.generateJasperPrint(report, getLayoutManager(), ds);

        // enalbe jsperViewr to view report
//        JasperViewer.viewReport(jp, false);

        if (reportParam.getExportType() == ExportType.PDF) {
            this.fileName = ReportExporter.exportReportPDF(jp, reportParam.getFilePath());
        }
        if (reportParam.getExportType() == ExportType.XLS) {
            this.fileName = ReportExporter.exportReportXLS(jp, reportParam.getFilePath());
        }

        return this;
    }

    protected LayoutManager getLayoutManager() {
        return new ClassicLayoutManager();
    }


    protected JRDataSource getDataSource(DynamicReport report) {
        Collection dataCollection = reportParam.getData();
        dataCollection = SortUtils.sortCollection(dataCollection, report.getColumns());
        JRDataSource ds = new JRBeanCollectionDataSource(dataCollection);
        return ds;
    }

    protected SimpleXlsReportConfiguration getXLSConfiguration() {
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setWhitePageBackground(false);
        configuration.setIgnoreGraphics(false);
        configuration.setIgnorePageMargins(true);
        return configuration;
    }


    public String getFileName() {
        return fileName;
    }

    private String getAbsolutePathForLogoImage() {
        ClassPathResource imgResource = new ClassPathResource(
            "img/logo.png");
//        URL res = getClass().getClassLoader().getResource("images/logo.png");
        File file = null;
        try {
            //file = Paths.get(res.toURI()).toFile();
            file = imgResource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }
}
