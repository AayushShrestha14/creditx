package com.sb.solutions.api.sharesecurity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.nepseCompany.entity.CustomerShareData;
import com.sb.solutions.core.enitity.BaseEntity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "share_security")
public class ShareSecurity extends BaseEntity<Long> {

    private String data;


    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CustomerShareData> customerShareData;
}
