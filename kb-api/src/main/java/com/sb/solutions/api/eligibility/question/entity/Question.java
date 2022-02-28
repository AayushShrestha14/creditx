package com.sb.solutions.api.eligibility.question.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity<Long> {

    private String description;

    private long maximumPoints;

    private long appearanceOrder;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "loan_config_id")
    private LoanConfig loanConfig;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

}
