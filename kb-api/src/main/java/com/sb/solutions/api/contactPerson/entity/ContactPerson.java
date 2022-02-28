package com.sb.solutions.api.contactPerson.entity;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.core.enitity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ContactPerson extends BaseEntity<Long> {

    private String name;
    private String contactNumber;
    private String email;
    private String functionalPosition;

    @ManyToOne
    private Province province;

    @ManyToOne
    private District district;

    @ManyToOne
    private MunicipalityVdc municipalityVdc;
}
