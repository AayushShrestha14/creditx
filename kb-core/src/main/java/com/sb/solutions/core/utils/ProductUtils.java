package com.sb.solutions.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author : Rujan Maharjan on  1/17/2020
 **/

public class ProductUtils {


    public static Boolean LAS;

    public static Boolean MEMO;

    public static Boolean ACCOUNT;

    public static Boolean OFFER_LETTER;

    public static Boolean ELIGIBILITY;

    public static Boolean DMS;

    public static Boolean NEP_TEMPLATE;

    public static Boolean CAD_LITE_VERSION;

    public static Boolean CBS_ENABLE;

    public static Boolean FULL_CAD;

    public static Boolean CHECK_LIST_LITE_VERSION;

    public static Boolean CUSTOMER_BASE_LOAN;
    public static Boolean CONFIGURE_LEGAL_DOCUMENT;


    private static Map<String, Object> productUtilsMap = new HashMap<>();


    @Value("${product.las}")
    public void setLas(Boolean las) {
        LAS = las;
        productUtilsMap.put("LAS", las);
    }

    @Value("${product.memo}")
    public void setMemo(Boolean memo) {
        MEMO = memo;
        productUtilsMap.put("MEMO", memo);
    }

    @Value("${product.account}")
    public void setAccount(Boolean account) {
        ACCOUNT = account;
        productUtilsMap.put("ACCOUNT", account);
    }

    @Value("${product.cadLiteVersion}")
    public void setCadDocUpload(Boolean cadDocUpload) {
        CAD_LITE_VERSION = cadDocUpload;
        productUtilsMap.put("CAD_LITE_VERSION", cadDocUpload);
    }

    @Value("${product.offerletter}")
    public void setOfferLetter(Boolean offerLetter) {
        OFFER_LETTER = offerLetter;
        productUtilsMap.put("OFFER_LETTER", offerLetter);
    }

    @Value("${product.eligibility}")
    public void setEligibility(Boolean eligibility) {
        ELIGIBILITY = eligibility;
        productUtilsMap.put("ELIGIBILITY", eligibility);
    }

    @Value("${product.dms}")
    public void setDms(Boolean dms) {
        DMS = dms;
        productUtilsMap.put("DMS", dms);
    }

    @Value("${product.nepTemplate}")
    public void setNepTemplate(Boolean nepTemplate) {
        NEP_TEMPLATE = nepTemplate;
        productUtilsMap.put("NEP_TEMPLATE", nepTemplate);
    }

    @Value("${product.loanApprovalHierarchyLevel}")
    public void setApprovalHierarchyForLoan(ApprovalType approvalType) {
        productUtilsMap.put("LOAN_APPROVAL_HIERARCHY_LEVEL", approvalType);
    }


    /**
     * Full cad represent work flow of cad respect to offer letter,legal document and disbursement
     * Each type have their own stages
     * to enable set value true
     **/

    @Value("${product.fullCad}")
    public void setFullCad(Boolean fullCad) {
        FULL_CAD = fullCad;
        productUtilsMap.put("FULL_CAD", fullCad);
    }

    @Value("${product.cbsEnable}")
    public void setCbsEnable(Boolean cbsEnable) {
        CBS_ENABLE = cbsEnable;
        productUtilsMap.put("CBS_ENABLE", cbsEnable);
    }

    @Value("${product.checklistLiteVersion}")
    public void setCheckListLiteVersion(Boolean value) {
        CHECK_LIST_LITE_VERSION = value;
        productUtilsMap.put("CHECK_LIST_LITE_VERSION", value);
    }

    @Value("${product.configureLegalDocument}")
    public void setConfigureLegalDocument(Boolean configureLegalDocument) {
        CONFIGURE_LEGAL_DOCUMENT = configureLegalDocument;
        productUtilsMap.put("CONFIGURE_LEGAL_DOCUMENT", configureLegalDocument);
    }

    @Value("${product.customerBaseLoan}")
    public void setCustomerBaseLoan(Boolean customerBaseLoan) {
        CUSTOMER_BASE_LOAN = customerBaseLoan;
        productUtilsMap.put("CUSTOMER_BASE_LOAN", customerBaseLoan);
    }

    public static Map<String, Object> getProductUtilsMap() {
        return productUtilsMap;
    }
}
