package com.sb.solutions.api.document.service;

import java.util.List;
import java.util.Map;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.api.document.entity.LoanCycle;
import com.sb.solutions.core.service.BaseService;


public interface DocumentService extends BaseService<Document> {

    List<Document> getByCycleContainingAndStatus(Long loanCycle, String statusName);

    Map<Object, Object> documentStatusCount();

    String saveList(List<Long> ids, LoanCycle loanCycle);

    List<Document> getByStatus(String statusName);

    String downloadAllDoc(String path, String sourcePathCustomer,String cadDocPath);
}
