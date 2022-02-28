package com.sb.solutions.api.crg.entity;

import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunil Babu Shrestha on 9/10/2020
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrgQuestion extends BaseEntity<Long> {
    private String description;

    private double maximumPoints;

    private long appearanceOrder;

    private Status status;

    private long crgGroupId;

    @ManyToOne
    @JoinColumn(name = "loan_config_id")
    private LoanConfig loanConfig;

    @OneToMany(mappedBy = "crgQuestion")
    private List<CrgAnswer> answers = new ArrayList<>();
}
