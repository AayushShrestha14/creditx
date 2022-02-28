package com.sb.solutions.api.valuator.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.api.branch.entity.Branch;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.State;
import com.sb.solutions.core.enums.Status;
import com.sb.solutions.core.enums.ValuatingField;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Where(clause = "status != 2")
public class Valuator extends BaseEntity<Long> implements Serializable {

    private String name;
    private String contactNo;
    private Status status;
    private State state;
    private String email;
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;
    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;
    @ManyToOne
    @JoinColumn(name = "municipality_vdc_id")
    private MunicipalityVdc municipalityVdc;
    private String streetName;
    private String wardNumber;

    private String valuatingField;
    private Date bankAssociateDate;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String inactiveComment;

    @ManyToMany
    private List<Branch> branch;

    private Boolean isAllBranch = false;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = ValuatingField.class, fetch = FetchType.EAGER)
    @CollectionTable(name="valuator_valuating_fields", joinColumns = @JoinColumn(name = "valuator_id"))
    @Column(name="valuator_fields")
    private Set<ValuatingField> valuatingFields;
}
