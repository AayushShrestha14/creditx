package com.sb.solutions.api.eligibility.applicant.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.api.eligibility.answer.entity.Answer;
import com.sb.solutions.api.eligibility.answer.entity.EligibilityAnswer;
import com.sb.solutions.api.eligibility.common.EligibilityStatus;
import com.sb.solutions.api.eligibility.document.entity.SubmissionDocument;
import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Applicant extends BaseEntity<Long> {

    private String fullName;

    private long age;

    private Date dob;

    private long phoneNumber;

    private String email;

    private double requestAmount;

    private String remarks;

    private EligibilityStatus eligibilityStatus;

    private long obtainedMarks;

    private double eligibleAmount;

    @Transient
    private double emiAmount;

    @ManyToOne
    @JoinColumn(name = "loan_config_id")
    private LoanConfig loanConfig;

    @ManyToMany
    @JoinTable(name = "applicant_answer",
        joinColumns = @JoinColumn(name = "applicant_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_id"))
    private List<Answer> answers = new ArrayList<>();

    @OneToMany
    @JoinTable(name = "applicant_document",
        joinColumns = @JoinColumn(name = "applicant_id"),
        inverseJoinColumns = @JoinColumn(name = "submission_document_id"))
    private List<SubmissionDocument> documents;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToMany(mappedBy = "applicant")
    @Cascade({CascadeType.PERSIST, CascadeType.MERGE})
    private List<EligibilityAnswer> eligibilityAnswers = new ArrayList<>();

}
