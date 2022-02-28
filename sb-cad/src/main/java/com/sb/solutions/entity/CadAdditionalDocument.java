package com.sb.solutions.entity;

import java.util.Date;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author : Rujan Maharjan on  1/12/2021
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CadAdditionalDocument extends BaseEntity<Long> {

    private String docName;

    private String docPath;

    private Date uploadOn;

    private String remarks;


}
