package com.sb.solutions.api.address.district.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.sb.solutions.api.address.province.entity.Province;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    private String nepaliName;

}
