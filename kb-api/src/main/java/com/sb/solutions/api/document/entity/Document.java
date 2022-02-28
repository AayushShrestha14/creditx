package com.sb.solutions.api.document.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.DocumentCheckType;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Document extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String displayName;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String url;

    @ManyToMany
    @JoinTable(name = "document_loan_cycle",
        joinColumns = {@JoinColumn(name = "document_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "loan_cycle_id", referencedColumnName = "id")})
    private Set<LoanCycle> loanCycle;

    private Status status;

    private DocumentCheckType checkType;

    private Boolean containsTemplate;
}
