package com.sb.solutions.api.loan.service;

import com.sb.solutions.api.loan.entity.CustomerOfferLetterPath;
import com.sb.solutions.api.user.entity.User;
import com.sb.solutions.core.service.BaseService;

import java.util.List;

/**
 * @author : Rujan Maharjan on  11/25/2020
 **/
public interface CustomerOfferLetterPathService extends BaseService<CustomerOfferLetterPath> {

    String updateApproveStatusAndApprovedBy(List<Long> ids);
}
