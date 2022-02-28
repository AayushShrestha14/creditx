package com.sb.solutions.api.incomeFromAccount.entity;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.sb.solutions.core.enitity.BaseEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class IncomeFromAccount extends BaseEntity<Long> {

    private String data;

}
