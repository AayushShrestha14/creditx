package com.sb.solutions.api.transactionService.service;

import com.sb.solutions.api.customer.entity.CustomerInfo;
import com.sb.solutions.api.loan.dto.CustomerDto;
import com.sb.solutions.core.exception.InvalidTransactionRequest;

public interface TransactionRequestService {

    CustomerInfo customerInfo(CustomerDto request) throws InvalidTransactionRequest;

//    FundTransferResponse fundTransfer(AccountTransferDto request) throws InvalidTransactionRequest;
}
