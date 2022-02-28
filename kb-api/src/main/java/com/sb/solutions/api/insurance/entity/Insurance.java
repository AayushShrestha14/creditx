package com.sb.solutions.api.insurance.entity;

import java.util.Date;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.utils.string.DoubleSerializer;
import com.sb.solutions.core.utils.string.NameFormatter;
import com.sb.solutions.core.utils.string.WordFormatter;

/**
 * @author Aashish shrestha on 6th. March, 2020
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Insurance extends BaseEntity<Long> {

    @JsonDeserialize(using = NameFormatter.class)
    private String company;

    @JsonSerialize(using = DoubleSerializer.class)
    private double insuredAmount;

    @JsonSerialize(using = DoubleSerializer.class)
    private double premiumAmount;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date issuedDate;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date expiryDate;

    @JsonDeserialize(using = WordFormatter.class)
    private String policyType;

    private String policyNumber;

    private String policyDocumentPath;
    private String remark;
    private String assetInsured;
}
