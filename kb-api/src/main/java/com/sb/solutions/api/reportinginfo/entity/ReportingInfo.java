package com.sb.solutions.api.reportinginfo.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

/**
 * @author Elvin Shrestha on 3/27/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportingInfo extends BaseEntity<Long> {

    // c = customer, f = facility
    private String type;
    private String name;

    private Status status;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ReportingInfoLevel> reportingInfoLevels;
}
