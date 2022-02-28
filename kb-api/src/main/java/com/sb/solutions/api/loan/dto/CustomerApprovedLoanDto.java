package com.sb.solutions.api.loan.dto;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author : Rujan Maharjan on  3/9/2021
 **/

@Data
public class CustomerApprovedLoanDto {

    @SerializedName("id")
    private Long id;

    @SerializedName("roleName")
    private String roleName;
    @SerializedName("facilityName")
    private String facilityName;

    @SerializedName("customerName")
    private String customerName;

    @SerializedName("proposedAmount")
    private BigDecimal proposedAmount;

}
