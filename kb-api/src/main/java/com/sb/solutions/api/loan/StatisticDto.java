package com.sb.solutions.api.loan;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.sb.solutions.core.enums.DocStatus;

@Data
@AllArgsConstructor
public class StatisticDto {

    BigDecimal totalAmount;

    DocStatus status;

    String loanType;

    Long files;
}
