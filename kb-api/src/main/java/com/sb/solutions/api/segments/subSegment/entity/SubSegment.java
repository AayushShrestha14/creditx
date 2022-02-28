package com.sb.solutions.api.segments.subSegment.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.loanConfig.entity.LoanConfig;
import com.sb.solutions.api.segments.segment.entity.Segment;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SubSegment extends BaseEntity<Long> {

    private String subSegmentName;

    private boolean isFunded;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    private Status status;

    @ManyToMany
    @JoinTable(name = "sub_segment_loan_config",
        joinColumns = {@JoinColumn(name = "sub_segment_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "loan_config_id", referencedColumnName = "id")})
    private Set<LoanConfig> loanConfig;

}
