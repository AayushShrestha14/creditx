package com.sb.solutions.api.nepalitemplate.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loan.entity.CustomerLoan;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.NepaliTemplateType;

/**
 * @author Elvin Shrestha on 1/22/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NepaliTemplate extends BaseEntity<Long> {

    @ManyToOne
    private CustomerLoan customerLoan;

    @Enumerated(value = EnumType.STRING)
    private NepaliTemplateType type;

    private String data;
}
