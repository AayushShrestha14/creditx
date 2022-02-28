package com.sb.solutions.api.vehiclesecurity.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.valuator.entity.Valuator;
import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author Elvin Shrestha on 1/16/20
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleSecurityValuator extends BaseEntity<Long> {

    private String vehicleName;
    private String model;
    private Date manufactureYear;
    private String registrationNumber;
    private Double valuationAmount;
    private String engineNumber;
    private String chassisNumber;
    private Date registrationDate;
    private String color;
    private String purpose;
    private String supplier;
    private Double downPayment;
    private Float loanExposure;
    private Double showroomCommission;
    @OneToOne
    private Valuator valuator;
    private Date valuatedDate;
    private String valuatorRepresentativeName;
    private String staffRepresentativeName;
}
