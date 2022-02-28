package com.sb.solutions.api.branch.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sb.solutions.api.address.district.entity.District;
import com.sb.solutions.api.address.municipalityVdc.entity.MunicipalityVdc;
import com.sb.solutions.api.address.province.entity.Province;
import com.sb.solutions.core.enitity.BaseEntity;
import com.sb.solutions.core.enums.Status;

/**
 * @author Rujan Maharjan on 2/13/2019
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class Branch extends BaseEntity<Long> implements Serializable {


    @NotNull(message = "Name should not be null")
    private String name;

    @Column(name = "branch_code")
    private String branchCode;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "province_id")
    @NotAudited
    private Province province;

    @ManyToOne
    @JoinColumn(name = "district_id")
    @NotAudited
    private District district;

    @ManyToOne
    @JoinColumn(name = "municipality_vdc_id")
    @NotAudited
    private MunicipalityVdc municipalityVdc;

    private String streetName;
    private String wardNumber;
    private String email;
    private String landlineNumber;
    private String locationPreview;


}
