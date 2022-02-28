package com.sb.solutions.api.customer.service;

import java.util.List;

import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.core.service.Service;

/**
 * @author : Rujan Maharjan on  8/25/2020
 **/
public interface CustomerGeneralDocumentService extends Service<CustomerGeneralDocument, Long> {

    List<CustomerGeneralDocument> findByCustomerInfoId(Long id);

    String deleteByDocId(Long id,Long customerInfoId,String path);

    CustomerGeneralDocument findByCustomerInfoIdAndDocumentId(Long customerInfoId, Long documentId);

    String deleteByDocIndex(Long id,Long customerInfoId,String path,Integer docIndex);

    CustomerGeneralDocument findFirstByCustomerInfoIdAndDocumentOrderByDocIndexDesc(Long customerInfoId, Long documentId);

}
