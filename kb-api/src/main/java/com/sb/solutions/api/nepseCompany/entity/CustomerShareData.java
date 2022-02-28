package com.sb.solutions.api.nepseCompany.entity;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.ShareType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
public class CustomerShareData extends BaseEntity<Long> {
    private String companyName;
    private Double amountPerUnit;
    private String companyCode;
    private ShareType shareType;
    private int totalShareUnit;
}
