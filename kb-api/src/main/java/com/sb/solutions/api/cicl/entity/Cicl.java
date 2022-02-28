package com.sb.solutions.api.cicl.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;
import java.util.Date;

/**
 * @author : Rujan Maharjan on  9/30/2020
 **/
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cicl extends BaseEntity<Long> {

    private String data;

    private String remarks;

    private String cibCharge;

    private String repaymentTrack;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone="GMT")
    private Date cibDate = new Date();

    private String checkedData;

}
