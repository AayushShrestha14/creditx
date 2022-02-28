package com.sb.solutions.api.loan.service;

import com.sb.solutions.api.loan.entity.CadDocument;
import com.sb.solutions.core.service.BaseService;

public interface CadDocumentService extends BaseService<CadDocument> {

    int deleteByLoanIdAndDocument(Long longId, Long docId);

    void deleteById(Long customerDocId);

}
