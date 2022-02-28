package com.sb.solutions.report.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * @author Sunil Babu Shrestha on 4/28/2020
 */
public class ReportExporter {

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ReportExporter.class);

    /**
     * The path to the file must exist.
     *
     * @param jp An instance of JasperPrint
     * @param path A path to the file.
     * @throws JRException Throws JRException
     * @throws FileNotFoundException Throws FileNotFoundException
     */
    public static String exportReportPDF(JasperPrint jp, String path)
        throws JRException, FileNotFoundException {
        logger.debug("Exporing report to: " + path);
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        JRPdfExporter exporter = new JRPdfExporter();
        String fileName = System.currentTimeMillis() + ".PDF";
        File outputFile = new File(dir + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);

        SimpleExporterInput simpleExporterInput = new SimpleExporterInput(jp);
        OutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(
            fos);

        exporter.setExporterInput(simpleExporterInput);
        exporter.setExporterOutput(simpleOutputStreamExporterOutput);

        exporter.exportReport();
        deleteFile(outputFile);
        logger.debug("Report exported: " + path);
        return fileName;
    }

    public static String exportReportXLS(JasperPrint jp, String path,
        SimpleXlsReportConfiguration configuration) throws JRException, FileNotFoundException {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        JRXlsExporter exporter = new JRXlsExporter();
        String fileName = System.currentTimeMillis() + ".XLS";
        File outputFile = new File(dir + "/" + fileName);
        FileOutputStream fos = new FileOutputStream(outputFile);

        exporter.setConfiguration(configuration);

        SimpleExporterInput simpleExporterInput = new SimpleExporterInput(jp);
        OutputStreamExporterOutput simpleOutputStreamExporterOutput = new SimpleOutputStreamExporterOutput(
            fos);

        exporter.setExporterInput(simpleExporterInput);
        exporter.setExporterOutput(simpleOutputStreamExporterOutput);

        exporter.exportReport();

        deleteFile(outputFile);
        logger.debug("Xlsx Report exported: " + path);
        return fileName;
    }

    public static String exportReportXLS(JasperPrint jp, String path)
        throws JRException, FileNotFoundException {
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setWhitePageBackground(false);
        configuration.setIgnoreGraphics(false);
        configuration.setIgnorePageMargins(true);

        return exportReportXLS(jp, path, configuration);
    }

    public static void exportReportHtml(JasperPrint jp, String path)
        throws JRException, FileNotFoundException {
        HtmlExporter exporter = new HtmlExporter();

        File outputFile = new File(path);
        FileOutputStream fos = new FileOutputStream(outputFile);

        SimpleExporterInput simpleExporterInput = new SimpleExporterInput(jp);
        SimpleHtmlExporterOutput simpleOutputStreamExporterOutput = new SimpleHtmlExporterOutput(
            fos);

        exporter.setExporterInput(simpleExporterInput);
        exporter.setExporterOutput(simpleOutputStreamExporterOutput);
        SimpleHtmlExporterConfiguration configuration = new SimpleHtmlExporterConfiguration();

        exporter.setConfiguration(configuration);

        exporter.exportReport();

        logger.debug("HTML Report exported: " + path);
    }

    private static synchronized void deleteFile(File outputFile) {
        new Thread(() -> {
            try {
                Thread.sleep(30000);
                outputFile.delete();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
