package com.sb.solutions.entity;

import javax.persistence.Entity;

import com.sb.solutions.enums.OfferDocType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author : Rujan Maharjan on  12/31/2020
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OfferDocument extends BaseEntity<Long> {

    private String docName;


    private String initialInformation;

    private String supportedInformation;

    private String pathSigned;

    private String draftPath;

    private OfferDocType offerDocType = OfferDocType.DEFAULT;

}
