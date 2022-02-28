package com.sb.solutions.api.collateralSiteVisit.entity;

import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

/**
 * Created by Mohammad Hussain on Jun, 2021
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SiteVisitDocument extends BaseEntity<Long> {
    private String docName;
    private String isPrintable;
    private String docPath;
    private String securityName;
    private Boolean isApproved;
    private Integer docDeleted = 0;
}
