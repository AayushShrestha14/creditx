package com.sb.solutions.api.collateralSiteVisit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sb.solutions.api.security.entity.Security;
import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Mohammad Hussain on May, 2021
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CollateralSiteVisit extends BaseEntity<Long> {

    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT")
    private LocalDate siteVisitDate;
    private String securityName;
    private String siteVisitJsonData;

    @ManyToOne
    @JoinColumn(name = "security_id")
    @JsonIgnore
    private Security security;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<SiteVisitDocument> siteVisitDocuments;

    private Integer collateralDeleted = 0;
}
