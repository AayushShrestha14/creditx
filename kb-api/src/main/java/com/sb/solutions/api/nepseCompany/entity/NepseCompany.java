package com.sb.solutions.api.nepseCompany.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.ShareType;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NepseCompany extends BaseEntity<Long> {
    private Status status = Status.ACTIVE;
    private String companyName;
    private Double amountPerUnit;
    private String companyCode;
    private ShareType shareType;
    private Double priceEarningRatio;
    private Double priceToBookValue;
    private Double dividendYield;
    private Double dividendPayoutRatio;



}
