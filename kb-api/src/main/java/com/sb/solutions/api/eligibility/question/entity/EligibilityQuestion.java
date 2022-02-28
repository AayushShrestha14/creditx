package com.sb.solutions.api.eligibility.question.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.sb.solutions.api.eligibility.criteria.entity.EligibilityCriteria;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class EligibilityQuestion extends BaseEntity<Long> {

    private String description;

    private String operandCharacter;

    private long appearanceOrder;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "eligibility_criteria_id")
    @JsonIgnore
    private EligibilityCriteria eligibilityCriteria;
}
