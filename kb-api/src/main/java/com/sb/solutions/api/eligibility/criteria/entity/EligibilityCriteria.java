package com.sb.solutions.api.eligibility.criteria.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import com.sb.solutions.api.eligibility.question.entity.EligibilityQuestion;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class EligibilityCriteria extends BaseEntity<Long> {

    private double percentageOfAmount;

    @OneToMany(mappedBy = "eligibilityCriteria")
    @Where(clause = "status != 2")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private List<EligibilityQuestion> questions = new ArrayList<>();

    private String formula;

    private Status status;

}
