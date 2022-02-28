package com.sb.solutions.api.loan.service;

import com.sb.solutions.api.loan.entity.CustomerDocument;
import com.sb.solutions.core.service.BaseService;

/**
 * @author yunish on 11/5/2019
 */
public interface CustomerDocumentService extends BaseService<CustomerDocument> {

    String deleteCustomerDocFromSystem(String path);

    void deleteById(Long customerDocId);

}
