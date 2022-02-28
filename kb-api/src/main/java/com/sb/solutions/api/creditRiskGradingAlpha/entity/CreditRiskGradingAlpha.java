package com.sb.solutions.api.creditRiskGradingAlpha.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * @author Amulya Shrestha on 7/30/2020
 **/

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreditRiskGradingAlpha extends BaseEntity<Long> {
    private String data;
}
