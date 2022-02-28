package com.sb.solutions.api.guarantor.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.core.enitity.BaseEntity;

/**
 * @author Elvin Shrestha on 2/27/2020
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GuarantorDetail extends BaseEntity<Long> {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Guarantor> guarantorList;
}
