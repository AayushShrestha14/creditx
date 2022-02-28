package com.sb.solutions.web.loan.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignOfferLetter {

    @NotNull(message = "Loan cannot be empty")
    private Long CustomerLoanId;

    @NotNull(message = "User cannot be empty")
    private Long userId;

    @NotNull(message = "Role cannot be empty")
    private Long roleId;
}
