package com.sb.solutions.api.loanTemplate.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 2/25/2019
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanTemplate extends BaseEntity<Long> {

    private @NotNull String name;
    private @NotNull String templateUrl;
    private Integer orderUrl;
    @Lob
    private String templateView;
    private Status status;
}
