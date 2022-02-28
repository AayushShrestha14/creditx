package com.sb.solutions.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.document.entity.Document;
import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author : Rujan Maharjan on  12/4/2020
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadFile extends BaseEntity<Long> {

    private Long customerLoanId;
    @OneToOne
    private Document cadDocument;
    private String path;

    private String initialInformation;

    private String supportedInformation;

    private Date uploadedDate;
    private Double amount;

    private String remarks;
}
