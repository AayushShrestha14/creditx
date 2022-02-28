package com.sb.solutions.api.address.province.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Province implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String nepaliName;

}
