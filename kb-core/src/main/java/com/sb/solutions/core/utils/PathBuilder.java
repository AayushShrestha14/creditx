package com.sb.solutions.core.utils;

import com.sb.solutions.core.utils.string.StringUtil;
import org.apache.commons.lang.StringUtils;

/**
 * this utility method is use for chaining folder path for json file or document file
 *
 * @author Sunil Babu Shrestha on 9/6/2019
 */
public class PathBuilder {

    private static final String FILE_SEPARATOR = "/";
    private String basePath;
    private String branchName;
    private String customerName;
    private String customerCitizenship;
    private String loanType;
    // action can be either "new" or "close" or "renew"
    private String action;
    private boolean jsonPath;


    public PathBuilder(String basePath) {
        this.basePath = basePath;
    }

    private static String getDigitsFromString(String citizenNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < citizenNumber.length(); i++) {
            char c = citizenNumber.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public PathBuilder withBranch(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public PathBuilder withCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public PathBuilder withCitizenship(String customerCitizenship) {
        this.customerCitizenship = customerCitizenship;
        return this;
    }

    public PathBuilder withLoanType(String loanType) {
        this.loanType = loanType;
        return this;
    }

    public PathBuilder withAction(String action) {
        this.action = action;
        return this;
    }

    public PathBuilder isJsonPath(boolean jsonPath) {
        this.jsonPath = jsonPath;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(this.basePath);
        return sb.append(branchName).append(FILE_SEPARATOR)
                .append(customerName.trim().replace(" ", "_")).append("_")
                .append(getDigitsFromString(customerCitizenship)).append(FILE_SEPARATOR)
                .append(loanType.trim().replace(" ", "_")).append(FILE_SEPARATOR)
                .append(action).append(FILE_SEPARATOR)
                .append(jsonPath ? "json/" : "doc/").toString();
    }

    public String buildAccountOpening() {
        StringBuilder sb = new StringBuilder(this.basePath);
        return sb.append(branchName)
                .append(FILE_SEPARATOR)
                .append(customerName.trim().replace(" ", "_"))
                .append("_")
                .append(getDigitsFromString(customerCitizenship)).append(FILE_SEPARATOR)
                .append("AccountOpening")
                .append(FILE_SEPARATOR)
                .append(jsonPath ? "json/" : "doc/")
                .toString();
    }

    public String buildBuildFormDownloadPath(String formName) {
        StringBuilder sb = new StringBuilder(this.basePath);
        sb.append("download/").append(formName).append(FILE_SEPARATOR);
        return sb.toString();
    }

    public String buildCustomerInfoBasePath(Long customerInfoId, String name, String branch,
                                            String customerType) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append(StringUtil.getStringWithoutWhiteSpaceAndWithCapitalize(branch))
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append(
                        customerInfoId + "-" + StringUtil.getStringWithoutWhiteSpaceAndWithCapitalize(name))
                .append(FILE_SEPARATOR)
                .append("Customer_Document")
                .append(FILE_SEPARATOR)
                .toString();
    }

    public String buildLoanDocumentUploadBasePath(Long customerInfoId, String customerName,
                                                  String branch,
                                                  String customerType, String loanType, String loanName) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append(StringUtil.getStringWithoutWhiteSpaceAndWithCapitalize(branch))
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append(
                        customerInfoId + "-" + StringUtil
                                .getStringWithoutWhiteSpaceAndWithCapitalize(customerName))
                .append(FILE_SEPARATOR)
                .append("Loan_Document")
                .append(FILE_SEPARATOR)
                .append(StringUtil.getStringWithoutWhiteSpaceAndWithCapitalize(loanName.toLowerCase()))
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(loanType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("doc")
                .append(FILE_SEPARATOR)
                .toString();
    }

    /**
     *  this is for Loan document upload and download
     */
    public String buildLoanDocumentUploadBasePathWithId(Long customerInfoId,
                                                        Long branchId,
                                                        String customerType,
                                                        String action,
                                                        Long loanConfigId) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Loan_Document")
                .append(FILE_SEPARATOR)
                .append("Loan-" + loanConfigId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(action).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("document")
                .append(FILE_SEPARATOR)
                .toString();
    }

    public String buildTempLoanDocumentUploadBasePathForNewLoan(Long customerInfoId,
                                                        Long branchId,
                                                        String customerType,
                                                        String action,
                                                        Long loanConfigId) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Loan_Document")
                .append(FILE_SEPARATOR)
                .append("Loan-" + loanConfigId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(action).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("tempDocument")
                .append(FILE_SEPARATOR)
                .toString();
    }


    public String buildLoanDocumentUploadBasePathWithLoanId(Long customerInfoId,
                                                        Long branchId,
                                                        String customerType,
                                                        String action,
                                                        Long loanConfigId, String LoanId) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Loan_Document")
                .append(FILE_SEPARATOR)
                .append("Loan-" + loanConfigId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(action).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("loan-loan-" + LoanId)
                .append(FILE_SEPARATOR)
                .append("doc")
                .append(FILE_SEPARATOR)
                .toString();
    }

    /**
     *  this is for customer document upload and download
     */
    public String buildCustomerInfoBasePathWithId(Long customerInfoId, Long branchId,
                                                  String customerType) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Customer_Document")
                .append(FILE_SEPARATOR)
                .toString();
    }

    /**
     *  this is for loan Cad document upload and download
     */
    public String buildCadLoanDocumentUploadBasePathWithId(Long customerInfoId,
                                                           Long branchId,
                                                           String customerType,
                                                           String action,
                                                           Long loanId) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Loan_Document")
                .append(FILE_SEPARATOR)
                .append("Loan-" + loanId)
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(action).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("Cad_Document")
                .append(FILE_SEPARATOR)
                .toString();
    }

    public String buildCustomerCadDocumentPath(
            Long branchId,
            Long customerInfoId,
            Long offerLetterId,
            String type) {
        return new StringBuilder(this.basePath)
                .append("CAD")
                .append(FILE_SEPARATOR)
                .append("Offer_doc")
                .append(FILE_SEPARATOR)
                .append("Branch-" + branchId)
                .append(FILE_SEPARATOR)
                .append("Customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Offer_Letter-" + offerLetterId)
                .append(FILE_SEPARATOR)
                .append(type)
                .append(FILE_SEPARATOR)
                .toString();
    }

    public String buildCustomerCadDocumentCheckListPath(
        Long branchId,
        Long customerInfoId,
        Long loanId,Long customerApprovedDocId ) {
        return new StringBuilder(this.basePath)
            .append("CAD")
            .append(FILE_SEPARATOR)
            .append("Doc_checkList")
            .append(FILE_SEPARATOR)
            .append("Branch-" + branchId)
            .append(FILE_SEPARATOR)
            .append("Customer-" + customerInfoId)
            .append(FILE_SEPARATOR)
            .append("Cad-"+customerApprovedDocId)
            .append(FILE_SEPARATOR)
            .append("Loan-" + loanId)
            .append(FILE_SEPARATOR)
            .toString();
    }

    public String buildCustomerAdditionalDocPath(
        Long branchId,
        Long customerInfoId,
        Long cadId) {
        return new StringBuilder(this.basePath)
            .append("CAD")
            .append(FILE_SEPARATOR)
            .append("additional-document")
            .append(FILE_SEPARATOR)
            .append("Branch-" + branchId)
            .append(FILE_SEPARATOR)
            .append("Customer-" + customerInfoId)
            .append(FILE_SEPARATOR)
            .append("cad-" + cadId)
            .append(FILE_SEPARATOR)
            .toString();
    }

    public String buildCustomerCadSccDocPath(
        Long branchId,
        Long customerInfoId
        ,Long customerApprovedDocId ) {
        return new StringBuilder(this.basePath)
            .append("CAD")
            .append(FILE_SEPARATOR)
            .append("SCC")
            .append(FILE_SEPARATOR)
            .append("Branch-" + branchId)
            .append(FILE_SEPARATOR)
            .append("Customer-" + customerInfoId)
            .append(FILE_SEPARATOR)
            .append("Cad-"+customerApprovedDocId)
            .append(FILE_SEPARATOR)
            .toString();
    }

    public String buildCustomerSiteVisitPaths(Long customerInfoId,
                                                  String customerType, Long securityId,
                                              Long collateralId) {
        return new StringBuilder(this.basePath)
                .append("customers")
                .append(FILE_SEPARATOR)
                .append(StringUtils.deleteWhitespace(customerType).toUpperCase())
                .append(FILE_SEPARATOR)
                .append("customer-" + customerInfoId)
                .append(FILE_SEPARATOR)
                .append("Security-"+securityId)
                .append(FILE_SEPARATOR)
                .append("collateral-"+collateralId)
                .append(FILE_SEPARATOR)
                .append("sitevisitdocument")
                .append(FILE_SEPARATOR)
                .toString();
    }


}
