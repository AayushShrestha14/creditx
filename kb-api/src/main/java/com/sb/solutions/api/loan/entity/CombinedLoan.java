package com.sb.solutions.api.loan.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author Elvin Shrestha on 8/25/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CombinedLoan extends BaseEntity<Long> {

    @Transient
    private List<CustomerLoan> loans;
}
