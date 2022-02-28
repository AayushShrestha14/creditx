package com.sb.solutions.api.customer.repository;

import java.util.List;

import com.sb.solutions.api.customer.entity.CustomerGeneralDocument;
import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.repository.BaseRepository;

/**
 * @author : Rujan Maharjan on  8/25/2020
 **/
public interface CustomerGeneralDocumentRepository extends
    BaseRepository<CustomerGeneralDocument, Long> {

    List<CustomerGeneralDocument> findByCustomerInfoId(Long id);

    CustomerGeneralDocument findCustomerGeneralDocumentByDocumentIdAndCustomerInfoId(Long id,Long customerInfoId);

    CustomerGeneralDocument findByCustomerInfoIdAndDocument(Long customerInfoId, Document document);

    CustomerGeneralDocument findCustomerGeneralDocumentByDocumentIdAndCustomerInfoIdAndDocIndex(Long id,Long customerInfoId, Integer docIndex);

    CustomerGeneralDocument findFirstByCustomerInfoIdAndDocumentOrderByDocIndexDesc(Long customerInfoId, Document document);

}
