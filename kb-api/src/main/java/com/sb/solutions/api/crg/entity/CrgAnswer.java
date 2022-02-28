package com.sb.solutions.api.crg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CrgAnswer extends BaseEntity<Long> {

    @NotNull(message = "Description is required.")
    private String description;

    private double points;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "crg_question_id")
    @JsonIgnore
    private CrgQuestion crgQuestion;

}
